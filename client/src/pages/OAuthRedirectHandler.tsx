import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { login } from "../features/auth/authSlice";
import { useNavigate } from "react-router-dom";
import { User } from "../types/User";

const OAuthRedirectHandler = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");
    const refreshToken = params.get("refreshToken");

    if (token && refreshToken) {
      fetch("http://localhost:8080/api/v0/auth/me", {
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      })
        .then(async (res) => {
          if (!res.ok) throw new Error("Failed to fetch user data");
          const user: User = await res.json();
          dispatch(login({ user, token, refreshToken }));
          navigate("/home");
        })
        .catch((err) => {
          console.error(err);
          navigate("/signin");
        })
        .finally(() => setLoading(false));
    } else {
      console.error("No token in URL");
      navigate("/signin");
    }
  }, [dispatch, navigate]);

  if (loading){
    return (
      <div>
        <h1>Loading...</h1>
      </div>
    );
  }

  return (
    <div>
      <h1>Signing in...</h1>
    </div>
  );
};

export default OAuthRedirectHandler;
