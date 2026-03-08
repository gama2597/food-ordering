import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { OrderResponse } from '../../../shared/models/order.models';
import { PaymentResponse } from '../../../shared/models/payment.models';

@Injectable({ providedIn: 'root' })
export class OrdersDashboardService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getMyOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(`${this.baseUrl}/api/v1/orders/me`);
  }

  getMyOrderById(orderId: number): Observable<OrderResponse> {
    return this.http.get<OrderResponse>(`${this.baseUrl}/api/v1/orders/${orderId}`);
  }

  requestPayment(orderId: number): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${this.baseUrl}/api/v1/orders/${orderId}/request-payment`, {});
  }

  getPaymentByOrderId(orderId: number): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(`${this.baseUrl}/api/v1/payments/order/${orderId}`);
  }
}
