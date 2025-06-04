import { createApi } from "@reduxjs/toolkit/query/react";
import { MovieDto, Movie, MovieItem, MovieDetails } from "../../types/Movie";
import rtkBaseQuery from "../rtkBaseQuery";
import { ApiRequestParams } from "@/types/ApiRequestParams";

export const movieApi = createApi({
  reducerPath: "movieApi",
  baseQuery: rtkBaseQuery,
  tagTypes: ["Movie"],
  endpoints: (builder) => ({
    getMovies: builder.query<
      { movies: MovieItem[]; totalCount: number },
      {
        page: number;
        limit: number;
        orderBy?: string;
        ratingFilter?: string;
        yearFilter?: number;
        categoryFilter?: string;
        titleFilter?: string;
      }
    >({
      query: ({
        page,
        limit,
        orderBy,
        ratingFilter,
        yearFilter,
        categoryFilter,
        titleFilter,
      }) => ({
        url: "/films",
        params: {
          page,
          limit,
          ...(orderBy ? { orderBy } : {}),
          ...(ratingFilter && ratingFilter.toLowerCase() !== "all"
            ? { ratingFilter }
            : {}),
          ...(yearFilter && yearFilter > 0 ? { yearFilter } : {}),
          ...(categoryFilter && categoryFilter.toLowerCase() !== "all"
            ? { categoryFilter }
            : {}),
          ...(titleFilter?.trim() ? { titleFilter } : {}),
        },
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
    getMovieById: builder.query<MovieDetails, number>({
      query: (id) => ({
        url: `/films/${id}`,
      }),
      providesTags: (_result, _error, id) => [{ type: "Movie", id }],
      keepUnusedDataFor: 300,
    }),
    createMovie: builder.mutation<Movie, MovieDto>({
      query: (data: MovieDto): ApiRequestParams => ({
        url: "/films",
        method: "POST",
        data,
      }),
      invalidatesTags: [{ type: "Movie", id: "LIST" }],
    }),
    updateMovie: builder.mutation<Movie, { id: number; data: MovieDto }>({
      query: ({ id, data }): ApiRequestParams => ({
        url: `/films/${id}`,
        method: "PUT",
        data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: "Movie", id },
        { type: "Movie", id: "LIST" },
      ],
    }),
    addCategoryToMovie: builder.mutation<
      Movie,
      { id: number; category: string }
    >({
      query: ({ id, category }): ApiRequestParams => ({
        url: `/films/${id}/categories`,
        method: "POST",
        data: {category},  
      }),
      invalidatesTags: (_result, _error, { id }) => [{ type: "Movie", id }],
    }),
    removeCategoryFromMovie: builder.mutation<Movie, { id: number; category: string }>({
      query: ({ id, category }) => ({
        url: `/films/${id}/categories/${category}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _error, { id }) => [{ type: "Movie", id }],
    }),
    addActorToMovie: builder.mutation<Movie, { id: number; actorId: number }>(
      {
        query: ({ id, actorId }): ApiRequestParams => ({
          url: `/films/${id}/actors`,
          method: "POST",
          data: {actorId},
        }),
        invalidatesTags: (_result, _error, { id }) => [{ type: "Movie", id }],
      }
    ),
    removeActorFromMovie: builder.mutation<Movie, {id: number, actorId: number}>({
      query: ({id, actorId}) => ({
        url: `/films/${id}/actors/${actorId}`,
        method: "DELETE"
      }),
      invalidatesTags: (_result, _error, {id}) => [{type: "Movie", id}],
    }),
    deleteMovieById: builder.mutation<void, number>({
      query: (id) => ({
        url: `/films/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: [{ type: "Movie", id: "LIST" }],
    }),
  }),
});

export const {
  useGetMoviesQuery,
  useGetMovieByIdQuery,
  useCreateMovieMutation,
  useUpdateMovieMutation,
  useDeleteMovieByIdMutation,
  useAddCategoryToMovieMutation,
  useRemoveCategoryFromMovieMutation,
  useAddActorToMovieMutation,
  useRemoveActorFromMovieMutation,
} = movieApi;
