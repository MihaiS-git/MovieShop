import { ApiRequestParams } from "@/types/ApiRequestParams";
import fetchApi from "../util/api";
import type { BaseQueryFn } from "@reduxjs/toolkit/query";
import { ErrorResponse } from "@/types/ErrorResponse";

const rtkBaseQuery: BaseQueryFn<ApiRequestParams<unknown>, unknown, ErrorResponse> = async (
  args,
  api
) => {
  const result = await fetchApi({ ...args, dispatch: api.dispatch });
  if (result.error) return { error: result.error as ErrorResponse };
  return { data: result.data };
};

export default rtkBaseQuery;
