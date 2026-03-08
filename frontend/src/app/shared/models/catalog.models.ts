export interface Restaurant {
  id: number;
  name: string;
  description: string;
  address: string;
  active: boolean;
}

export interface Product {
  id: number;
  restaurantId: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
}
