import { createApi } from "@reduxjs/toolkit/query/react";
import rtkBaseQuery from "../rtkBaseQuery";
import { UserDetails, UserItem, UserUpdateRequestDto } from "@/types/User";
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
    getUsers: builder.query<
      { users: UserItem[]; totalCount: number },
      {
        page: number;
        limit: number;
        orderBy?: string;
        roleFilter?: string;
        searchFilter?: string;
        enabledFilter?: string;
        accountNonExpiredFilter?: string;
        accountNonLockedFilter?: string;
        credentialsNonExpiredFilter?: string;
      }
    >({
      query: ({
        page,
        limit,
        orderBy,
        roleFilter,
        searchFilter,
        enabledFilter,
        accountNonExpiredFilter,
        accountNonLockedFilter,
        credentialsNonExpiredFilter,
      }) => ({
        url: "/users",
        params: {
          page,
          limit,
          ...(orderBy ? { orderBy } : {}),
          ...(roleFilter && roleFilter.toLowerCase() !== "all"
            ? { roleFilter }
            : {}),
          ...(searchFilter && searchFilter.trim() !== ""
            ? { searchFilter }
            : {}),
          ...(enabledFilter ? { enabledFilter } : {}),
          ...(accountNonExpiredFilter ? { accountNonExpiredFilter } : {}),
          ...(accountNonLockedFilter ? { accountNonLockedFilter } : {}),
          ...(credentialsNonExpiredFilter
            ? { credentialsNonExpiredFilter }
            : {}),
        },
      }),
      /* providesTags: ["User"], */
      providesTags: (result) => 
        result?.users
      ? [
        ...result.users.map((u) => ({
          type: "User" as const,
          id: u.id,
        })),
        {type: "User", id: "LIST"},
      ]
      : [{type: "User", id: "LIST"}],
      keepUnusedDataFor: 300,
    }),
    deleteUserById: builder.mutation<void, number>({
      query: (id) => ({
        url: `/users/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: [{type: "User", id: "LIST"}],
    }),
  }),
});

export const {
  useGetUserByEmailQuery,
  useUpdateUserMutation,
  useGetUsersQuery,
  useDeleteUserByIdMutation,
} = userApi;
