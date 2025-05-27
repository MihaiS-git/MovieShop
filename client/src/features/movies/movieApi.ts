import { createApi } from "@reduxjs/toolkit/query/react";
import fetchApi from "../../util/api";
import { MovieDto, Movie, MovieItem } from "../../types/Movie";

export const movieApi = createApi({
  reducerPath: "movieApi",
  baseQuery: async ({ url, method = "GET", data, params }, api) => {
    const { dispatch } = api;
    const result = await fetchApi({ url, method, data, params, dispatch });

    if (result.error) return { error: result.error };
    return { data: result.data };
  },
  tagTypes: ["Movie"],
  endpoints: (builder) => ({
    getMovies: builder.query<
      { movies: MovieItem[]; totalCount: number },
      { page: number; limit: number }
    >({
      query: ({ page, limit }) => ({
        url: "/films",
        params: { page, limit },
      }),
      providesTags: (result) =>
        result?.movies
          ? [
              ...result.movies.map((m) => ({
                type: "Movie" as const,
                id: m.id,
              })),
              { type: "Movie", id: "LIST" },
            ]
          : [{ type: "Movie", id: "LIST" }],
      keepUnusedDataFor: 300,
    }),
    getMovieById: builder.query<Movie, number>({
      query: (id) => ({
        url: `/films/${id}`,
      }),
      providesTags: (_result, _error, id) => [{ type: "Movie", id }],
      keepUnusedDataFor: 300,
    }),
    createMovie: builder.mutation<Movie, MovieDto>({
      query: (data: MovieDto) => ({
        url: "/films",
        method: "POST",
        data,
      }),
      invalidatesTags: [{ type: "Movie", id: "LIST" }],
    }),
    updateMovie: builder.mutation<Movie, { id: number; data: MovieDto }>({
      query: ({ id, data }) => ({
        url: `/films/${id}`,
        method: "PUT",
        data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: "Movie", id },
        { type: "Movie", id: "LIST" },
      ],
    }),
    deleteMovieById: builder.mutation<void, number>({
      query: (id) => ({
        url: `/films/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: [{ type: "Movie", id: "LIST" }],
    }),
    searchMoviesByTitle: builder.query<Movie[], string>({
      query: (title) => ({
        url: `/films/search`,
        params: {title},
      }),
    }),
  }),
});

export const {
  useGetMoviesQuery,
  useGetMovieByIdQuery,
  useCreateMovieMutation,
  useUpdateMovieMutation,
  useDeleteMovieByIdMutation,
  useSearchMoviesByTitleQuery,
  useLazySearchMoviesByTitleQuery,
} = movieApi;
