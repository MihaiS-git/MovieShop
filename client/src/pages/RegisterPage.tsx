import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v0";

const RegisterPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    if(password !== confirmPassword){
      alert("The passwords do not match!");
      return;
    }

    try {
      const response = await fetch(`${API_URL}/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        alert("Registration successful!");
        navigate("/signin");
      } else {
        alert("Registration failed. Please try again.");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-red-500 dark:bg-brown-950 px-4">
      <h1 className="text-2xl font-bold mb-6 text-gold-500 dark:text-red-500">
        Register
      </h1>
      <form onSubmit={handleRegister} className="w-full max-w-sm space-y-4">
        <label htmlFor="email">
          Email
          <input
            id="email"
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full px-4 py-2 border border-charcoal-800 rounded-lg bg-gray-100 mb-2"
            disabled={loading}
          />
        </label>

        <label htmlFor="password">
          Password
          <input
            id="password"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-4 py-2 border border-charcoal-800 rounded-lg bg-gray-100 mb-2"
            disabled={loading}
          />
        </label>

        <label htmlFor="confirmPassword">
          Confirm Password
          <input
            id="confirmPassword"
            type="password"
            placeholder="Confirm Password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className="w-full px-4 py-2 border border-charcoal-800 rounded-lg bg-gray-100 mb-2"
            disabled={loading}
          />
        </label>

        <button
          type="submit"
          className="w-full bg-gray-200 text-charcoal-800 px-6 py-3 mt-4 rounded-lg hover:bg-gray-300 transition"
          disabled={loading}
        >
          {loading ? "Registering..." : "Register"}
        </button>
      </form>
    </div>
  );
};

export default RegisterPage;
