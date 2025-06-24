import { createApi } from "@reduxjs/toolkit/query/react";
import rtkBaseQuery from "../rtkBaseQuery";
import { StoreDetails, StoreItem } from "@/types/Store";

export const storeApi = createApi({
  reducerPath: "storeApi",
  baseQuery: rtkBaseQuery,
  tagTypes: ["Store"],
  endpoints: (builder) => ({
    getStores: builder.query<
    {stores: StoreItem[], totalCount: number},
    {
      page: number;
      limit: number;
      orderBy?: string;
      countryFilter?: string;
      cityFilter?: string;
    }
    >({
      query: ({
        page, 
        limit, 
        orderBy, 
        countryFilter, 
        cityFilter,
      }) => ({
        url: "/stores",
        params: {
          page,
          limit,
          ...(orderBy ? {orderBy} : {}),
          ...(countryFilter?.trim() ? {countryFilter} : {}),
          ...(cityFilter?.trim() ? {cityFilter} : {}),
        },
      }),
      providesTags: (result) => 
        result?.stores
       ? [
        ...result.stores.map((s) => ({
            type: "Store" as const,
            id: s.id,
        })),
        {type: "Store", id: "LIST"},
      ]
      : [{type: "Store", id: "LIST"}],
      keepUnusedDataFor: 300,
    }),
    getStoreById: builder.query<StoreDetails, number>({
      query: (id) => ({
        url: `/stores/${id}`,
      }),
      providesTags: (_result, _error, id) => [{type: "Store", id}],
      keepUnusedDataFor: 300,
    }),
  }),
});

export const { 
  useGetStoresQuery, 
  useGetStoreByIdQuery 
} = storeApi;