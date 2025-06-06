import { ErrorResponse } from "react-router-dom";
import { login, logout } from "../features/auth/authSlice";
import { ApiRequestParams } from "@/types/ApiRequestParams";
import { User } from "@/types/User";

interface RefreshResponse {
  token: string;
  refreshToken: string;
  user: User;
}

// Standard API response structure
interface ApiResponse<T = unknown> {
  data?: T;
  error?: ErrorResponse;
}

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v0";

// fetchApi handles API requests and includes authentication logic
const fetchApi = async ({
  url,
  method = "GET",
  data,
  params,
  requiresAuth = true,
  dispatch,
}: ApiRequestParams): Promise<ApiResponse> => {
  let auth;
  try {
    auth = JSON.parse(localStorage.getItem("auth") || "{}");
  } catch {
    auth = {};
  }
  const headers: HeadersInit = {
    "Content-Type": "application/json",
  };

  // Add the authorization header if a token is available
  if (requiresAuth && auth?.token) {
    headers["Authorization"] = `Bearer ${auth.token}`;
  }

  const queryString = params
    ? `?${new URLSearchParams(
        Object.entries(params).reduce((acc, [key, value]) => {
          acc[key] = String(value);
          return acc;
        }, {} as Record<string, string>)
      ).toString()}`
    : "";
  const requestUrl = `${BASE_URL}${url}${queryString}`;

  try {
    // Make the fetch request
    const response = await fetch(requestUrl, {
      method,
      headers,
      body: data ? JSON.stringify(data) : undefined,
    });

    if (!response.ok) {
      if (response.status === 401) {
        // Handle token expiration or invalidation
        return handleUnauthorizedError(response, {
          url,
          method,
          data,
          params,
          dispatch,
        });
      }
      const errorText = await response.text();
      return {
        data: { status: response.status, data: errorText || "Request failed" },
      };
    }

    return { data: await response.json() };
  } catch (error) {
    const err = error as { status?: number; message: string };
    return { data: { status: err.status || 500, data: err.message } };
  }
};

// Function to handle unauthorized errors (token expiration or invalidation)
const handleUnauthorizedError = async (
  response: Response,
  { url, method, data, params, dispatch }: ApiRequestParams
): Promise<ApiResponse> => {
  const auth = JSON.parse(localStorage.getItem("auth") || "{}");
  const refreshToken = auth?.refreshToken;

  if (!refreshToken) throw new Error("No refresh token");

  try {
    // Make a request to refresh the token
    const refreshResponse = await fetch(`${BASE_URL}/auth/refresh-token`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ refreshToken }),
    });

    if (!refreshResponse.ok) {
      throw new Error("Failed to refresh token");
    }

    // Parse the new token from the response
    const { token, refreshToken: newRefreshToken, user } = (await refreshResponse.json()) as RefreshResponse;
    const updatedAuth = {
      user,
      token,
      refreshToken: newRefreshToken,
    };

    // Save the new token to localStorage
    localStorage.setItem("auth", JSON.stringify(updatedAuth));

    // Dispatch the updated auth data to the Redux store
    dispatch?.(login(updatedAuth));

    // Retry the original request with the new token
    return fetchApi({
      url,
      method,
      data,
      params,
      dispatch,
      requiresAuth: true,
    });
  } catch (error) {
    console.error("Token refresh failed:", error);
    localStorage.clear();
    dispatch?.(logout());
    window.location.href = "/signin"; // Redirect to sign-in if token refresh fails
    return Promise.reject("Unauthorized");
  }
};

export default fetchApi;
