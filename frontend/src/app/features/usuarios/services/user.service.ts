import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CreateUserAddressRequest, UpdateUserAddressRequest, UserAddress, UserProfile } from '../../../shared/models/user.models';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiBaseUrl;

  getMyProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.baseUrl}/api/v1/users/me`);
  }

  getMyAddresses(): Observable<UserAddress[]> {
    return this.http.get<UserAddress[]>(`${this.baseUrl}/api/v1/users/me/addresses`);
  }

  addAddress(payload: CreateUserAddressRequest): Observable<UserAddress> {
    return this.http.post<UserAddress>(`${this.baseUrl}/api/v1/users/me/addresses`, payload);
  }

  updateAddress(addressId: number, payload: UpdateUserAddressRequest): Observable<UserAddress> {
    return this.http.put<UserAddress>(`${this.baseUrl}/api/v1/users/me/addresses/${addressId}`, payload);
  }

  deleteAddress(addressId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/v1/users/me/addresses/${addressId}`);
  }
}
