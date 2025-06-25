import { MovieInventoryItem } from "./Movie";

export interface Inventory {
  id: number;
  filmId: number;
  storeId: number;
  lastUpdate: string;
  rentalIds: number[];
}

export interface InventoryItem {
  id: number;
  storeId: number;
  film: MovieInventoryItem;
  lastUpdate: string;
}

export interface InventoryPageResponse {
  inventories: InventoryItem[];
  totalCount: number;
  currentPage: number;
  pageSize: number;
  totalPages: number;
  isLastPage: boolean;
}
