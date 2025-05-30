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
import AdminUsers from "./pages/admin/users/AdminUsers";
import EditMovies from "./pages/admin/movies/AdminMoviesList";
import AddMovie from "./pages/admin/movies/AddMovie";
import EditReviews from "./pages/admin/reviews/EditReviews";
import AddReview from "./pages/admin/reviews/AddReview";
import EditMoviePage from "./pages/admin/movies/EditMoviePage";

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
            children: [
              {
                path: "edit",
                element: <EditMovies/>,
              },
              {
                path: "edit/:id",
                element: <EditMoviePage />
              },
              {
                path: "add",
                element: <AddMovie/>
              }
            ],
          },
          {
            path: "users",
            children: [
              {
                path: "edit",
                element: <AdminUsers/>
              },
            ],
          },
          {
            path: "orders",
                        children: [
              {
                path: "edit",
                element: <AdminOrders/>
              },
            ],
          },
          {
            path: "reviews",
                        children: [
              {
                path: "edit",
                element: <EditReviews/>
              },
              {
                path: "add",
                element: <AddReview/>
              }
            ],
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
