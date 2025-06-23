import { RootState } from "@/app/store";
import { JSX } from "react";
import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

function RequireAdmin({children}: {children: JSX.Element}){
    const auth = useSelector((state: RootState) => state.auth);

    if(!auth?.isAuthenticated){
        return <Navigate to="/signin" replace />;
    }

    if(auth.user?.role !== "ADMIN"){
        alert("Access denied.")
        return <Navigate to="/" replace/>
    }

    return children;
};

export default RequireAdmin;