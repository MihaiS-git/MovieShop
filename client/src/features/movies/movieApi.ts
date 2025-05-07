import { createApi } from "@reduxjs/toolkit/query/react";
import fetchApi from "../../util/api";
import { Movie } from "../../types/Movie";

// Define the movie API with RTK Query
export const movieApi = createApi({
  reducerPath: "movieApi",
  baseQuery: async ({ url, method = "GET", data, params }, api) => {
    // Get the dispatch from api
    const { dispatch } = api;
    // Call the fetchApi utility and pass dispatch along
    const result = await fetchApi({ url, method, data, params, dispatch });

    if (result.error) return { error: result.error };
    return { data: result.data };
  },
  endpoints: (builder) => ({
    // Define a query to get movies
    getMovies: builder.query<Movie[], void>({
      query: () => ({ url: "/films" }),
      keepUnusedDataFor: 300, // Cache the data for 5 minutes
    }),
  }),
});

// Export the custom hook to use the movieApi
export const { useGetMoviesQuery } = movieApi;
