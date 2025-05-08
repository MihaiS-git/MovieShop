import "./App.css";
import HomePage from "./pages/HomePage";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import RootLayout from "./components/Root";
import SigninPage from "./pages/SigninPage";
import OAuthRedirectHandler from "./pages/OAuthRedirectHandler";
import { useDispatch } from "react-redux";
import { useEffect } from "react";
import { login } from "./features/auth/authSlice";
import RegisterPage from "./pages/RegisterPage";
import MovieListPage from "./pages/MovieListPage";

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
        path: "movies",
        element: <MovieListPage />,
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
        if (authState?.user && authState?.token && !authState?.isAuthenticated) {
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
