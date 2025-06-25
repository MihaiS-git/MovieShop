import { createApi } from "@reduxjs/toolkit/query/react";
import rtkBaseQuery from "../rtkBaseQuery";
import { InventoryPageResponse } from "@/types/Inventory";

export const inventoryApi = createApi({
  reducerPath: "inventoryApi",
  baseQuery: rtkBaseQuery,
  tagTypes: ["Inventory"],
  endpoints: (builder) => ({
    getInventoriesByStoreId: builder.query<
      InventoryPageResponse,
      { 
        storeId: number; 
        page: number; 
        limit: number; 
        orderBy?: string 
    }
    >({
      query: ({ 
        storeId, 
        page, 
        limit, 
        orderBy 
    }) => ({
        url: `/inventory/store/${storeId}`,
        params: { 
            page, 
            limit, 
            ...(orderBy) ? {orderBy} : {},
        },
      }),
      providesTags: (_result, _error, {storeId}) => [
        { type: "Inventory", id: storeId },
      ],
      keepUnusedDataFor: 300,
    }),
  }),
});

export const {
  useGetInventoriesByStoreIdQuery,
  useLazyGetInventoriesByStoreIdQuery,
} = inventoryApi;
