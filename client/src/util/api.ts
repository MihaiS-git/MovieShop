import axios from "axios";
import { store } from "../app/store";
import { logout, login } from "../features/auth/authSlice";

interface RefreshResponse {
  token: string;
}

const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v0";

const api = axios.create({
  baseURL: BASE_URL,
});

api.interceptors.request.use((config) => {
  const auth = JSON.parse(localStorage.getItem("auth") || "{}");
  if (auth?.token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${auth.token}`;
  }
  return config;
});

api.interceptors.response.use(
  (res) => res,
  async (err) => {
    const originalRequest = err.config;
    if (err.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const auth = JSON.parse(localStorage.getItem("auth") || "{}");
        const refreshToken = auth?.refreshToken;

        if (!refreshToken) throw new Error("No refresh token");

        const response = await axios.post<RefreshResponse>(
          `${BASE_URL}/auth/refresh-token`,
          refreshToken,
          {
            headers: { "Content-Type": "application/json" },
          }
        );

        const newToken = response.data.token;

        const updatedAuth = { ...auth, token: newToken };
        localStorage.setItem("auth", JSON.stringify(updatedAuth));
        store.dispatch(login(updatedAuth));

        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        console.error("Token refresh failed:", refreshError);
        localStorage.clear();
        store.dispatch(logout());
        window.location.href = "/signin";
      }
    }

    return Promise.reject(err);
  }
);

export default api;
