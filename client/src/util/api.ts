import { AppDispatch } from "../app/store";
import { login } from "../features/auth/authSlice";

// Interfaces for error handling and response formatting
interface ErrorResponse {
  status: number;
  data: string;
}

interface RefreshResponse {
  token: string;
}

// Params for API requests
interface ApiRequestParams {
  url: string;
  method?: string;
  data?: Record<string, unknown>; // Body data
  params?: Record<string, string | number>; // URL query params
  requiresAuth?: boolean; // Whether authentication is required
  dispatch?: AppDispatch; // Dispatch for updating Redux state
}

// Standard API response structure
interface ApiResponse {
  data?: unknown;
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
  const auth = JSON.parse(localStorage.getItem("auth") || "{}");
  const headers: HeadersInit = {
    "Content-Type": "application/json",
  };

  // Add the authorization header if a token is available
  if (requiresAuth && auth?.token) {
    headers["Authorization"] = `Bearer ${auth.token}`;
  }

  const queryString = params
    ? `?${new URLSearchParams(params as Record<string, string>).toString()}`
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
        return handleUnauthorizedError(response, { url, method, data, params, dispatch });
      }
      throw new Error("Request failed");
    }

    return { data: await response.json() };
  } catch (error) {
    const err = error as { status?: number; message: string };
    return { error: { status: err.status || 500, data: err.message } };
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
    const { token } = (await refreshResponse.json()) as RefreshResponse;
    const updatedAuth = { ...auth, token };

    // Save the new token to localStorage
    localStorage.setItem("auth", JSON.stringify(updatedAuth));

    // Dispatch the updated auth data to the Redux store
    dispatch?.(login(updatedAuth)); // Make sure `login` updates the state

    // Retry the original request with the new token
    return fetchApi({ url, method, data, params, dispatch });
  } catch (error) {
    console.error("Token refresh failed:", error);
    localStorage.clear();
    window.location.href = "/signin"; // Redirect to sign-in if token refresh fails
    return Promise.reject("Unauthorized");
  }
};

export default fetchApi;
