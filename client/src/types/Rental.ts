export interface Rental {
  id: number;
  rentalDate: string;
  inventoryId: number;
  customerId: number;
  returnDate: string;
  staffId: number;
  lastUpdate: string;
  rentalPeriod: number;
  paymentIds: number[];
}

export interface RentalInventoryItem {
  id: number;
  rentalDate: string;
  inventoryId: number;
  customerId: number;
  returnDate: string;
  staffId: number;
  lastUpdate: string;
  rentalPeriod: number;
  paymentIds: number[];
}
