import { createApi } from "@reduxjs/toolkit/query/react";
import rtkBaseQuery from "../rtkBaseQuery";
import { UserDetails, UserUpdateRequestDto } from "@/types/User";
import { ApiRequestParams } from "@/types/ApiRequestParams";

export const userApi = createApi({
  reducerPath: "userApi",
  baseQuery: rtkBaseQuery,
  tagTypes: ["User"],
  endpoints: (builder) => ({
    getUserByEmail: builder.query<UserDetails, string>({
      query: (email) => ({
        url: `/users/email/${email}`,
      }),
      providesTags: (_result, _error, email) => [{ type: "User", email }],
      keepUnusedDataFor: 300,
    }),
    updateUser: builder.mutation<
      UserDetails,
      { id: number; data: UserUpdateRequestDto }
    >({
      query: ({ id, data }): ApiRequestParams => ({
        url: `/users/${id}`,
        method: "PUT",
        data,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: "User", id },
        { type: "User", id: "LIST" },
      ],
    }),
  }),
});

export const { 
  useGetUserByEmailQuery,
  useUpdateUserMutation,
} = userApi;
