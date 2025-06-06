import { City } from "./City";

export interface AddressRequestDto {
    address: string;
    address2?: string | "";
    district: string;
    city: string;
    country: string;
    postalCode: string;
    phone: string;
    userId?: number;
}

export interface Address {
    id: number;
    address: string;
    address2: string;
    district: string;
    city: City;
    postalCode: string;
    phone: string;
}