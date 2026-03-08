export interface UserProfile {
  id: number;
  authUserId: string;
  email: string;
  firstName: string;
  lastName: string;
  phone: string;
  active: boolean;
}

export interface UserAddress {
  id: number;
  userId: number;
  label: string;
  line1: string;
  line2?: string;
  district: string;
  city: string;
  reference?: string;
  primaryAddress: boolean;
  active: boolean;
}

export interface CreateUserAddressRequest {
  label: string;
  line1: string;
  line2?: string;
  district: string;
  city: string;
  reference?: string;
  primaryAddress: boolean;
}

export type UpdateUserAddressRequest = CreateUserAddressRequest;
