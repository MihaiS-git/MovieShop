import { Navigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { JSX } from "react";
import { RootState } from "@/app/store";

function RequireAuth({ children }: { children: JSX.Element }) {
  const auth = useSelector((state: RootState) => state.auth);

  if (!auth?.isAuthenticated) {
    return <Navigate to="/signin" replace />;
  }

  return children;
};

export default RequireAuth;
