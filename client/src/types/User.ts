import { Address } from "./Address";
import { CustomerStore } from "./Store";

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
  STAFF
}

export const ROLE = ["ADMIN", "CUSTOMER", "STAFF"];

export interface UserDetails {
    id: number;
    email: string;
    role: Role;
    firstName: string;
    lastName: string;
    address: Address;
    store: CustomerStore;
    picture: string;
    rentals: number[];
    accountNonExpired: boolean;
    accountNonLocked: boolean;
    credentialsNonExpired: boolean;
    enabled: boolean;
    createAt: string;
    lastUpdate: string;
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

export interface UserUpdateRequestDto{
    firstName: string;
    lastName: string;
}

export interface ManagerItem{
  id: number;
  firstName: string;
  lastName: string;
}