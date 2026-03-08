import { Component, OnInit, inject } from '@angular/core';
import { DatePipe } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { OrdersDashboardService } from '../services/orders-dashboard.service';
import { OrderResponse } from '../../../shared/models/order.models';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [CardModule, TableModule, TagModule, DatePipe],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent implements OnInit {
  private readonly ordersDashboardService = inject(OrdersDashboardService);

  orders: OrderResponse[] = [];
  readonly stats = [
    { label: 'Microservicios activos', value: '4' },
    { label: 'Pedidos registrados', value: '0' },
    { label: 'Topologia', value: 'Gateway + 3 servicios' }
  ];

  ngOnInit(): void {
    this.ordersDashboardService.getMyOrders().subscribe((orders) => {
      this.orders = orders;
      this.stats[1] = { ...this.stats[1], value: String(orders.length) };
    });
  }

  statusSeverity(status: string): 'success' | 'warn' | 'info' | 'danger' {
    if (status === 'PAID') {
      return 'success';
    }
    if (status === 'PAYMENT_PENDING') {
      return 'warn';
    }
    if (status === 'CANCELLED') {
      return 'danger';
    }
    return 'info';
  }
}
