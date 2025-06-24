import { Address } from "./Address";
import { Inventory } from "./Inventory";
import { ManagerItem, UserDetails } from "./User";

export interface CustomerStore {
  id: number;
  managerStaffId: number;
  lastUpdate: string;
}

export interface Store {
  id: number;
  managerStaffId: number;
  addressId: number;
  lastUpdate: string;
}

export interface StoreItem {
  id: number;
  managerStaff: ManagerItem;
  address: Address;
  lastUpdate: string;
}

export interface StoreDetails {
  id: number;
  managerStaff: UserDetails;
  address: Address;
  inventories: Inventory[];
  lastUpdate: string;
}