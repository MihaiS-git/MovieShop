import { createApi } from "@reduxjs/toolkit/query/react";
import fetchApi from "../../util/api";
import { Movie } from "../../types/Movie";

export const movieApi = createApi({
  reducerPath: "movieApi",
  baseQuery: async ({ url, method = "GET", data, params }, api) => {
    const { dispatch } = api;
    const result = await fetchApi({ url, method, data, params, dispatch });

    if (result.error) return { error: result.error };
    return { data: result.data };
  },
  endpoints: (builder) => ({
    getMovies: builder.query<{movies: Movie[], totalCount: number}, {page: number, limit: number}>({
      query: ({page, limit}) => ({
         url: "/films", 
         params: { page, limit },
        }),
      keepUnusedDataFor: 300,
    }),
    getMovieById: builder.query<Movie, number>({
      query: (id) => ({
        url: `/films/${id}`,
      }),
      keepUnusedDataFor: 300,
    }),
  }),
});

export const { useGetMoviesQuery, useGetMovieByIdQuery } = movieApi;
