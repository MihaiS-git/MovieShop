import { Country } from "./Country";

export interface City {
    id: number;
    name: string;
    country: Country;
    lastUpdate: string;
}