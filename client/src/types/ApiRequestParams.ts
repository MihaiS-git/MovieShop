import { AppDispatch } from "@/app/store";

export interface ApiRequestParams<TData = unknown> {
  url: string;
  method?: string;
  data?: TData;       // POST/PUT body
  params?: Record<string, string | number>; // URL query params
  requiresAuth?: boolean;               // defaults to true
  dispatch?: AppDispatch;              // for triggering redux actions (e.g. refresh login)
}