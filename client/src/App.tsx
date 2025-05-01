import "./App.css";
import HomePage from "./pages/HomePage";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import RootLayout from "./components/Root";

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
    ]
  }
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
