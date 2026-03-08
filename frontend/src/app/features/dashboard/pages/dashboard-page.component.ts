import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { MessageService } from 'primeng/api';
import { Subscription, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { OrdersDashboardService } from '../services/orders-dashboard.service';
import { OrderResponse } from '../../../shared/models/order.models';
import { PaymentResponse } from '../../../shared/models/payment.models';
import { DeliveryResponse } from '../../../shared/models/delivery.models';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [CardModule, TableModule, TagModule, ButtonModule, DialogModule, DatePipe],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent implements OnInit, OnDestroy {
  private readonly ordersDashboardService = inject(OrdersDashboardService);
  private readonly messageService = inject(MessageService);
  private readonly pollingSubscriptions = new Map<number, Subscription>();

  orders: OrderResponse[] = [];
  payingOrderId?: number;
  loadingPaymentOrderId?: number;
  paymentByOrderId: Record<number, PaymentResponse | undefined> = {};
  selectedPayment?: PaymentResponse;
  paymentDialogVisible = false;
  loadingDeliveryOrderId?: number;
  selectedDelivery?: DeliveryResponse;
  deliveryDialogVisible = false;
  readonly stats = [
    { label: 'Microservicios activos', value: '5' },
    { label: 'Pedidos registrados', value: '0' },
    { label: 'Topologia', value: 'Gateway + 4 servicios' }
  ];

  ngOnInit(): void {
    this.loadOrders();
  }

  ngOnDestroy(): void {
    this.pollingSubscriptions.forEach((subscription) => subscription.unsubscribe());
    this.pollingSubscriptions.clear();
  }

  canRequestPayment(order: OrderResponse): boolean {
    return order.status === 'CREATED';
  }

  requestPayment(order: OrderResponse): void {
    if (!this.canRequestPayment(order)) {
      return;
    }

    this.payingOrderId = order.id;
    this.ordersDashboardService.requestPayment(order.id).subscribe({
      next: (updatedOrder) => {
        this.payingOrderId = undefined;
        this.upsertOrder(updatedOrder);
        this.startPollingOrder(order.id);
        this.messageService.add({
          severity: 'success',
          summary: 'Pago solicitado',
          detail: `El pedido #${order.id} paso a PAYMENT_PENDING.`
        });
      },
      error: (error) => {
        this.payingOrderId = undefined;
        this.messageService.add({
          severity: 'error',
          summary: 'No se pudo solicitar el pago',
          detail: error?.error?.message ?? 'Intenta nuevamente en unos segundos.'
        });
      }
    });
  }

  canLoadPayment(order: OrderResponse): boolean {
    return ['PAID', 'ASSIGNED', 'DELIVERING', 'DELIVERED', 'CANCELLED'].includes(order.status);
  }

  canLoadDelivery(order: OrderResponse): boolean {
    return ['ASSIGNED', 'DELIVERING', 'DELIVERED'].includes(order.status);
  }

  loadPayment(order: OrderResponse): void {
    if (!this.canLoadPayment(order)) {
      return;
    }

    this.loadingPaymentOrderId = order.id;
    this.ordersDashboardService.getPaymentByOrderId(order.id).subscribe({
      next: (payment) => {
        this.loadingPaymentOrderId = undefined;
        this.paymentByOrderId[order.id] = payment;
        this.selectedPayment = payment;
        this.paymentDialogVisible = true;
      },
      error: (error) => {
        this.loadingPaymentOrderId = undefined;
        this.messageService.add({
          severity: 'warn',
          summary: 'Pago aun no disponible',
          detail: error?.error?.message ?? 'El pago todavia no fue registrado. Reintenta en unos segundos.'
        });
      }
    });
  }

  private loadOrders(): void {
    this.ordersDashboardService.getMyOrders().subscribe((orders) => {
      this.orders = orders;
      this.stats[1] = { ...this.stats[1], value: String(orders.length) };
      orders
        .filter((order) => ['PAYMENT_PENDING', 'PAID', 'ASSIGNED', 'DELIVERING'].includes(order.status))
        .forEach((order) => this.startPollingOrder(order.id));
    });
  }

  private startPollingOrder(orderId: number): void {
    if (this.pollingSubscriptions.has(orderId)) {
      return;
    }

    const subscription = interval(2500)
      .pipe(switchMap(() => this.ordersDashboardService.getMyOrderById(orderId)))
      .subscribe({
        next: (order) => {
          this.upsertOrder(order);
          if (order.status === 'CANCELLED' || order.status === 'DELIVERED') {
            this.stopPollingOrder(orderId);
            this.ordersDashboardService.getPaymentByOrderId(orderId).subscribe({
              next: (payment) => {
                this.paymentByOrderId[orderId] = payment;
                this.selectedPayment = payment;
              }
            });
            if (order.status === 'DELIVERED') {
              this.ordersDashboardService.getDeliveryByOrderId(orderId).subscribe({
                next: (delivery) => {
                  this.selectedDelivery = delivery;
                }
              });
            }
          }
        },
        error: () => {
          this.stopPollingOrder(orderId);
        }
      });

    this.pollingSubscriptions.set(orderId, subscription);
  }

  closePaymentDialog(): void {
    this.paymentDialogVisible = false;
  }

  loadDelivery(order: OrderResponse): void {
    if (!this.canLoadDelivery(order)) {
      return;
    }

    this.loadingDeliveryOrderId = order.id;
    this.ordersDashboardService.getDeliveryByOrderId(order.id).subscribe({
      next: (delivery) => {
        this.loadingDeliveryOrderId = undefined;
        this.selectedDelivery = delivery;
        this.deliveryDialogVisible = true;
      },
      error: (error) => {
        this.loadingDeliveryOrderId = undefined;
        this.messageService.add({
          severity: 'warn',
          summary: 'Entrega aun no disponible',
          detail: error?.error?.message ?? 'La entrega todavia no fue registrada. Reintenta en unos segundos.'
        });
      }
    });
  }

  closeDeliveryDialog(): void {
    this.deliveryDialogVisible = false;
  }

  private stopPollingOrder(orderId: number): void {
    const subscription = this.pollingSubscriptions.get(orderId);
    if (!subscription) {
      return;
    }
    subscription.unsubscribe();
    this.pollingSubscriptions.delete(orderId);
  }

  private upsertOrder(order: OrderResponse): void {
    const index = this.orders.findIndex((item) => item.id === order.id);
    if (index >= 0) {
      this.orders[index] = order;
      return;
    }
    this.orders = [order, ...this.orders];
  }

  statusSeverity(status: string): 'success' | 'warn' | 'info' | 'danger' {
    if (status === 'PAID') {
      return 'success';
    }
    if (status === 'PAYMENT_PENDING') {
      return 'warn';
    }
    if (status === 'ASSIGNED' || status === 'DELIVERING') {
      return 'info';
    }
    if (status === 'DELIVERED') {
      return 'success';
    }
    if (status === 'CANCELLED') {
      return 'danger';
    }
    return 'info';
  }

  paymentStatusSeverity(status: string): 'success' | 'danger' | 'info' {
    if (status === 'APPROVED') {
      return 'success';
    }
    if (status === 'REJECTED') {
      return 'danger';
    }
    return 'info';
  }
}
