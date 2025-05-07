import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { login } from "../features/auth/authSlice";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v0";
const API_ROOT_URL = import.meta.env.VITE_API_ROOT_URL || "http://localhost:8080";

const SigninPage = () => {
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleGoogleLogin = () => {
    setLoading(true);
    window.location.href = `${API_ROOT_URL}/oauth2/authorization/google`;
  };

  const handleEmailLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        alert(errorData.message || "Invalid credentials");
        return;
      }

      const data = await response.json();

      if (response.ok) {
        dispatch(login({ user: data.user, token: data.token, refreshToken: data.refreshToken }));
        navigate("/home");
      } else {
        alert("Invalid credentials");
      }
    } catch (error) {
      console.error("Login failed:", error);
      alert("Login failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-red-500 dark:bg-brown-950 px-4">
      <h1 className="text-2xl font-bold mb-6 text-gold-500 dark:text-red-500">
        Login
      </h1>

      <button
        onClick={handleGoogleLogin}
        className="bg-gray-200 text-charcoal-800 px-6 py-3 rounded-lg
         hover:bg-gray-300 transition mb-4 w-full max-w-sm flex items-center justify-center"
        disabled={loading}
      >
        {loading ? (
          "Loading..."
        ) : (
          <div className="flex items-center justify-between space-x-2">
            <img src="google-logo.svg" alt="google logo" className="w-5 h-5" />
            <span>Sign in with Google</span>
          </div>
        )}
      </button>

      <div className="flex items-center gap-4 my-6 w-full max-w-sm">
        <div className="w-full border-t border-gold-500 dark:border-red-500 h-px"></div>
        <span className="text-base text-gold-500 dark:text-red-500">OR</span>
        <div className="w-full border-t border-gold-500 dark:border-red-500 h-px"></div>
      </div>

      <form onSubmit={handleEmailLogin} className="w-full max-w-sm space-y-4">
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full px-4 py-2 border border-charcoal-800 rounded-lg bg-gray-100"
          disabled={loading}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full px-4 py-2 border border-charcoal-800 rounded-lg bg-gray-100"
          disabled={loading}
        />
        <button
          type="submit"
          className="w-full bg-gray-200 text-charcoal-800 px-6 py-3 rounded-lg hover:bg-gray-300 transition"
          disabled={loading}
        >
          {loading ? "Logging in..." : "Log in"}
        </button>
      </form>

      <div className="mt-4 text-charcoal-800 dark:text-red-500">
        <span>Don't have an account?</span>
        <button
          onClick={() => navigate("/register")}
          className="text-gold-500 hover:underline ml-1"
        >
          Register here
        </button>
      </div>
    </div>
  );
};

export default SigninPage;
