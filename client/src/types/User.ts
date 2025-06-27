import { Address } from "./Address";

export interface User {
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  picture: string;
}

export enum Role {
  ADMIN,
  CUSTOMER,
  STAFF,
}

export const ROLE = ["ADMIN", "CUSTOMER", "STAFF"];

export interface UserDetails {
  id: number;
  email: string;
  role: Role;
  firstName: string;
  lastName: string;
  address: Address;
  storeId: number;
  picture: string;
  rentalIds: number[];
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
  createAt: string;
  lastUpdate: string;
  customerPaymentIds: number[];
  staffPaymentIds: number[];
}

export interface UserItem {
  id: number;
  email: string;
  role: Role;
  firstName: string;
  lastName: string;
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
  createAt: string;
  lastUpdate: string;
}

export interface UserUpdateRequestDto {
  firstName: string;
  lastName: string;
}

export interface ManagerItem {
  id: number;
  firstName: string;
  lastName: string;
}

export interface UserAccountUpdateRequestDto {
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  enabled: boolean;
}
