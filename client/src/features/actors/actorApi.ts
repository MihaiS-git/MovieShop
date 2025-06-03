import { Actor } from "@/types/Actor";
import { createApi } from "@reduxjs/toolkit/query/react";
import rtkBaseQuery from "../rtkBaseQuery";

export const actorApi = createApi({
  reducerPath: "actorApi",
  baseQuery: rtkBaseQuery,
  tagTypes: ["Actor"],
  endpoints: (builder) => ({
    getActorById: builder.query<Actor, number>({
      query: (id) => ({
        url: `/actors/${id}`,
      }),
      providesTags: (_result, _error, id) => [{ type: "Actor", id }],
      keepUnusedDataFor: 300,
    }),
    searchActorsByName: builder.query<Actor[], string>({
      query: (searchName) => ({
        url: `/actors/search?searchName=${searchName}`,
      }),
    }),
  }),
});

export const { 
    useGetActorByIdQuery,
    useSearchActorsByNameQuery, 
    useLazySearchActorsByNameQuery
} = actorApi;
