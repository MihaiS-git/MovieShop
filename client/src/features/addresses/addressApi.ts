import { ApiRequestParams } from "@/types/ApiRequestParams";
import rtkBaseQuery from "../rtkBaseQuery";
import { createApi } from "@reduxjs/toolkit/query/react";
import { Address, AddressRequestDto } from "@/types/Address";

export const addressApi = createApi({
  reducerPath: "addressApi",
  baseQuery: rtkBaseQuery,
  tagTypes: ["Address"],
  endpoints: (builder) => ({
    createAddress: builder.mutation<Address, AddressRequestDto>({
      query: (data): ApiRequestParams => ({
        url: "/addresses",
        method: "POST",
        data,
      }),
      invalidatesTags: [{ type: "Address", id: "LIST" }],
    }),
    updateAddressById: builder.mutation<
      Address,
      { id: number; data: AddressRequestDto }
    >({
      query: ({ id, data }): ApiRequestParams => ({
        url: `/addresses/${id}`,
        method: "PUT",
        data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: "Address", id },
        { type: "Address", id: "LIST" },
      ],
    }),
  }),
});

export const {
    useCreateAddressMutation,
    useUpdateAddressByIdMutation,
} = addressApi;