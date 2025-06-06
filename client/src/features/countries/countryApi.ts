import { createApi } from "@reduxjs/toolkit/query/react";
import rtkBaseQuery from "../rtkBaseQuery";
import { Country } from "@/types/Country";

export const countryApi = createApi({
  reducerPath: "countryApi",
  baseQuery: rtkBaseQuery,
  tagTypes: ["Country"],
  endpoints: (builder) => ({
    getCountries: builder.query<Country[], void>({
      query: () => ({
        url: "/countries",
      }),
      providesTags: ["Country"],
      keepUnusedDataFor: 300,
    }),
  }),
});

export const { useGetCountriesQuery } = countryApi;
