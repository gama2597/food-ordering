import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessageService } from 'primeng/api';
import { CatalogService } from '../services/catalog.service';
import { Product, Restaurant } from '../../../shared/models/catalog.models';
import { OrderService } from '../services/order.service';
import { CreateOrderItemRequest } from '../../../shared/models/order.models';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-productos-page',
  standalone: true,
  imports: [CardModule, TableModule, ButtonModule, InputNumberModule, FormsModule],
  templateUrl: './productos-page.component.html',
  styleUrl: './productos-page.component.scss'
})
export class ProductosPageComponent implements OnInit {
  private readonly catalogService = inject(CatalogService);
  private readonly orderService = inject(OrderService);
  private readonly messageService = inject(MessageService);
  private readonly authService = inject(AuthService);

  restaurants: Restaurant[] = [];
  selectedRestaurant?: Restaurant;
  products: Product[] = [];
  quantities: Record<number, number> = {};
  creatingOrder = false;
  deletingRestaurantId?: number;
  deletingProductId?: number;
  lastCreatedOrderId?: number;

  ngOnInit(): void {
    this.catalogService.getRestaurants().subscribe((restaurants) => {
      this.restaurants = restaurants;
      if (restaurants.length > 0) {
        this.onRestaurantChange(restaurants[0]);
      }
    });
  }

  onRestaurantChange(restaurant: Restaurant): void {
    this.selectedRestaurant = restaurant;
    this.lastCreatedOrderId = undefined;
    this.quantities = {};
    this.catalogService.getProductsByRestaurant(restaurant.id).subscribe((products) => {
      this.products = products;
    });
  }

  canDeleteRestaurants(): boolean {
    return this.authService.hasAnyRole(['ADMIN']);
  }

  canDeleteProducts(): boolean {
    return this.authService.hasAnyRole(['ADMIN', 'RESTAURANT']);
  }

  deleteRestaurant(restaurant: Restaurant): void {
    if (!this.canDeleteRestaurants()) {
      return;
    }

    const confirmed = window.confirm(`Se desactivara el restaurante "${restaurant.name}". Esta accion no se puede deshacer.`);
    if (!confirmed) {
      return;
    }

    this.deletingRestaurantId = restaurant.id;
    this.catalogService.deleteRestaurant(restaurant.id).subscribe({
      next: () => {
        this.deletingRestaurantId = undefined;
        this.messageService.add({
          severity: 'success',
          summary: 'Restaurante desactivado',
          detail: `El restaurante ${restaurant.name} fue desactivado.`
        });

        this.catalogService.getRestaurants().subscribe((restaurants) => {
          this.restaurants = restaurants;
          const currentSelectionStillExists = this.selectedRestaurant && restaurants.some((item) => item.id === this.selectedRestaurant?.id);
          if (currentSelectionStillExists) {
            return;
          }

          this.selectedRestaurant = undefined;
          this.products = [];
          this.quantities = {};

          if (restaurants.length > 0) {
            this.onRestaurantChange(restaurants[0]);
          }
        });
      },
      error: (error) => {
        this.deletingRestaurantId = undefined;
        this.messageService.add({
          severity: 'error',
          summary: 'No se pudo desactivar restaurante',
          detail: error?.error?.message ?? 'Intenta nuevamente en unos segundos.'
        });
      }
    });
  }

  deleteProduct(product: Product): void {
    if (!this.canDeleteProducts()) {
      return;
    }

    const confirmed = window.confirm(`Se desactivara el producto "${product.name}". Esta accion no se puede deshacer.`);
    if (!confirmed) {
      return;
    }

    this.deletingProductId = product.id;
    this.catalogService.deleteProduct(product.id).subscribe({
      next: () => {
        this.deletingProductId = undefined;
        this.products = this.products.filter((item) => item.id !== product.id);
        delete this.quantities[product.id];
        this.messageService.add({
          severity: 'success',
          summary: 'Producto desactivado',
          detail: `El producto ${product.name} fue desactivado.`
        });
      },
      error: (error) => {
        this.deletingProductId = undefined;
        this.messageService.add({
          severity: 'error',
          summary: 'No se pudo desactivar producto',
          detail: error?.error?.message ?? 'Intenta nuevamente en unos segundos.'
        });
      }
    });
  }

  hasItemsToOrder(): boolean {
    return this.getSelectedItems().length > 0;
  }

  createOrder(): void {
    if (!this.selectedRestaurant || !this.hasItemsToOrder()) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Pedido incompleto',
        detail: 'Selecciona al menos un producto con cantidad mayor a cero.'
      });
      return;
    }

    this.creatingOrder = true;
    this.orderService
      .createOrder({
        restaurantId: this.selectedRestaurant.id,
        items: this.getSelectedItems()
      })
      .subscribe({
        next: (order) => {
          this.creatingOrder = false;
          this.lastCreatedOrderId = order.id;
          this.quantities = {};
          this.messageService.add({
            severity: 'success',
            summary: 'Pedido creado',
            detail: `Pedido #${order.id} registrado. Solicita el pago desde Dashboard cuando desees.`
          });
        },
        error: (error) => {
          this.creatingOrder = false;
          this.messageService.add({
            severity: 'error',
            summary: 'No se pudo crear el pedido',
            detail: error?.error?.message ?? 'Intenta nuevamente en unos segundos.'
          });
        }
      });
  }

  private getSelectedItems(): CreateOrderItemRequest[] {
    return this.products
      .filter((product) => product.available)
      .map((product) => ({
        productId: product.id,
        quantity: Number(this.quantities[product.id] ?? 0)
      }))
      .filter((item) => item.quantity > 0);
  }
}
