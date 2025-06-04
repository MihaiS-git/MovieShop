import { ApiRequestParams } from "@/types/ApiRequestParams";
import fetchApi from "../util/api";
import type { BaseQueryFn } from "@reduxjs/toolkit/query";
import { ErrorResponse } from "react-router-dom";

const rtkBaseQuery: BaseQueryFn<ApiRequestParams<unknown>, unknown, ErrorResponse> = async (
  args,
  api
) => {
  const result = await fetchApi({ ...args, dispatch: api.dispatch });
  if (result.error) return { error: result.error };
  return { data: result.data };
};

export default rtkBaseQuery;
