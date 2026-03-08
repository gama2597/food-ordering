export interface DeliveryResponse {
  id: number;
  orderId: number;
  customerAuthUserId: string;
  status: 'ASSIGNED' | 'DELIVERING' | 'DELIVERED' | string;
  assignedAt?: string;
  startedAt?: string;
  deliveredAt?: string;
  createdAt: string;
  updatedAt: string;
}
