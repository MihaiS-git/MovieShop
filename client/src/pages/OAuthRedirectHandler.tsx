import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { login } from "../features/auth/authSlice";
import { useNavigate } from "react-router-dom";

const OAuthRedirectHandler = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    if (token) {
      fetch("http://localhost:8080/api/v0/auth/me", {
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      })
        .then(async (res) => {
          if (!res.ok) throw new Error("Failed to fetch user data");
          const user = await res.json();
          dispatch(login({ user, token }));
          navigate("/home");
        })
        .catch((err) => {
          console.error(err);
          navigate("/signin");
        });
    } else {
      console.error("No token in URL");
      navigate("/signin");
    }
  }, [dispatch, navigate]);

  return (
    <div>
      <h1>Signing in...</h1>
    </div>
  );
};

export default OAuthRedirectHandler;
