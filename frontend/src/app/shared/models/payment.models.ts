export interface PaymentResponse {
  id: number;
  orderId: number;
  customerAuthUserId: string;
  amount: number;
  currency: string;
  status: 'APPROVED' | 'REJECTED' | string;
  reason: string;
  createdAt: string;
  updatedAt: string;
}
