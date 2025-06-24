export interface Inventory {
    id: number;
    filmId: number;
    storeId: number;
    lastUpdate: string;
    rentalIds: number[];
}