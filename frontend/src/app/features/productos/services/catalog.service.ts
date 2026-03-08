import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Product, Restaurant } from '../../../shared/models/catalog.models';

@Injectable({ providedIn: 'root' })
export class CatalogService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(`${this.baseUrl}/api/v1/catalog/restaurants`);
  }

  getProductsByRestaurant(restaurantId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/api/v1/catalog/products/restaurant/${restaurantId}`);
  }

  deleteRestaurant(restaurantId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/v1/catalog/restaurants/${restaurantId}`);
  }

  deleteProduct(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/v1/catalog/products/${productId}`);
  }
}
