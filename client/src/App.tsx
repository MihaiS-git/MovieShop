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
import MovieListPage from "./pages/movies/MovieListPage";
import MovieDetailsPage from "./pages/movies/MovieDetailsPage";
import AdminLayout from "./pages/admin/AdminLayout";
import AdminDashboard from "./pages/admin/dashboard/AdminDashboard";
import AdminOrders from "./pages/admin/orders/AdminOrders";
import AdminUsersListPage from "./pages/admin/users/AdminUsersListPage";
import AdminMoviesListPage from "./pages/admin/movies/AdminMoviesListPage";
import AddMovie from "./pages/admin/movies/AddMovie";
import EditReviews from "./pages/admin/reviews/EditReviews";
import AddReview from "./pages/admin/reviews/AddReview";
import EditMoviePage from "./pages/admin/movies/EditMoviePage";
import UserAccountPage from "./pages/account/UserAccountPage";
import RequireNoAuth from "./components/auth/RequireNoAuth";
import RequireAuth from "./components/auth/RequireAuth";

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
        element: (
            <HomePage />
        ),
      },
      {
        path: "signin",
        element: (
          <RequireNoAuth>
            <SigninPage />
          </RequireNoAuth>
        ),
      },
      {
        path: "oauth2/redirect",
        element: (
          <RequireNoAuth>
            <OAuthRedirectHandler />
          </RequireNoAuth>
        ),
      },
      {
        path: "register",
        element: (
          <RequireNoAuth>
            <RegisterPage />
          </RequireNoAuth>
        ),
      },
      {
        path: "admin",
        element: (
          <RequireAuth>
            <AdminLayout />
          </RequireAuth>
        ),
        children: [
          {
            path: "movies",
            children: [
              {
                path: "edit",
                element: (
                  <RequireAuth>
                    <AdminMoviesListPage />
                  </RequireAuth>
                ),
              },
              {
                path: "edit/:id",
                element: (
                  <RequireAuth>
                    <EditMoviePage />
                  </RequireAuth>
                ),
              },
              {
                path: "add",
                element: (
                  <RequireAuth>
                    <AddMovie />
                  </RequireAuth>
                ),
              },
            ],
          },
          {
            path: "users",
            children: [
              {
                path: "edit",
                element: (
                  <RequireAuth>
                    <AdminUsersListPage />
                  </RequireAuth>
                ),
              },
            ],
          },
          {
            path: "orders",
            children: [
              {
                path: "edit",
                element: (
                  <RequireAuth>
                    <AdminOrders />
                  </RequireAuth>
                ),
              },
            ],
          },
          {
            path: "reviews",
            children: [
              {
                path: "edit",
                element: (
                  <RequireAuth>
                    <EditReviews />
                  </RequireAuth>
                ),
              },
              {
                path: "add",
                element: (
                  <RequireAuth>
                    <AddReview />
                  </RequireAuth>
                ),
              },
            ],
          },
          {
            path: "dashboard",
            element: (
              <RequireAuth>
                <AdminDashboard />
              </RequireAuth>
            ),
          },
        ],
      },
      {
        path: "movies",
        element: (
            <MovieListPage />
        ),
      },
      {
        path: "movies/:id",
        element: (
            <MovieDetailsPage />
        ),
      },
      {
        path: "account",
        element: (
          <RequireAuth>
            <UserAccountPage />
          </RequireAuth>
        ),
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
