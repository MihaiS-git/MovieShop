import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useEffect } from "react";
import { login } from "./features/auth/authSlice";

import "./App.css";
import HomePage from "./pages/HomePage";
import RootLayout from "./components/Root";
import SigninPage from "./pages/SigninPage";
import OAuthRedirectHandler from "./pages/OAuthRedirectHandler";
import RegisterPage from "./pages/RegisterPage";
import MovieListPage from "./pages/MovieListPage";
import MovieDetailsPage from "./pages/MovieDetailsPage";
import AdminLayout from "./pages/admin/AdminLayout";
import AdminDashboard from "./pages/admin/dashboard/AdminDashboard";
import AdminReviews from "./pages/admin/reviews/AdminReviews";
import AdminOrders from "./pages/admin/orders/AdminOrders";
import AdminUsers from "./pages/admin/users/AdminUsers";
import AdminMovies from "./pages/admin/movies/AdminMovies";

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: "home",
        element: <HomePage />,
      },
      {
        path: "signin",
        element: <SigninPage />,
      },
      {
        path: "oauth2/redirect",
        element: <OAuthRedirectHandler />,
      },
      {
        path: "register",
        element: <RegisterPage />,
      },
      {
        path: "admin",
        element: <AdminLayout />,
        children: [
          {
            path: "movies",
            element: <AdminMovies />,
          },
          {
            path: "users",
            element: <AdminUsers />,
          },
          {
            path: "orders",
            element: <AdminOrders />,
          },
          {
            path: "reviews",
            element: <AdminReviews />,
          },
          {
            path: "dashboard",
            element: <AdminDashboard />,
          },
        ],
      },
      {
        path: "movies",
        element: <MovieListPage />,
      },
      {
        path: "movies/:id",
        element: <MovieDetailsPage />,
      },
    ],
  },
]);

function App() {
  const dispatch = useDispatch();

  useEffect(() => {
    const storedAuth = localStorage.getItem("auth");
    if (storedAuth) {
      try {
        const authState = JSON.parse(storedAuth);
        if (
          authState?.user &&
          authState?.token &&
          !authState?.isAuthenticated
        ) {
          dispatch(login(authState));
        } else {
          localStorage.removeItem("auth");
        }
      } catch (error: unknown) {
        console.error("Failed to parse auth state from localStorage", error);
        localStorage.removeItem("auth");
      }
    }
  }, [dispatch]);

  return <RouterProvider router={router} />;
}

export default App;
