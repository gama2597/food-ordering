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

  restaurants: Restaurant[] = [];
  selectedRestaurant?: Restaurant;
  products: Product[] = [];
  quantities: Record<number, number> = {};
  creatingOrder = false;
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
            detail: `Pedido #${order.id} registrado correctamente.`
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
