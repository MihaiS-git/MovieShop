import { Address } from "./Address";
import { CustomerStore } from "./Store";

export interface User {
  email: string;
  name: string;
  role: string;
  picture: string;
}

export interface AuthResponse {
  token: string | null;
  user: User | null;
}

export enum Role {
  ADMIN,
  CUSTOMER,
  STAFF
}

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

export interface UserUpdateRequestDto{
    firstName: string;
    lastName: string;
}