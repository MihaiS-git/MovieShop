import { JSX } from "react";
import { RootState } from "../../app/store";
import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

function RequireNoAuth({ children }: { children: JSX.Element }) {
  const auth = useSelector((state: RootState) => state.auth);

  if (auth?.isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return children;
}

export default RequireNoAuth;