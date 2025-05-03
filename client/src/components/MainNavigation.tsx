import { Link } from "react-router-dom";
import useLogout from "../features/auth/useAuth";

const MainNavigation = () => {
  const handleLogout = useLogout();
  
  return (
    <>
      <nav className="hidden xl:flex items-center justify-center w-full p-4 text-red-500 dark:text-red-500 text-md">
        <ul className="flex flex-row space-x-4">
          <li>
            <Link to="/movies" className="hover:text-red-hover active:text-red-hover">Movies</Link>
          </li>
          <li>
            <Link to="/cart" className="hover:text-red-hover active:text-red-hover">Cart</Link>
          </li>
          <li>
            <Link to="/account" className="hover:text-red-hover active:text-red-hover">Account</Link>
          </li>
          <li>
            <Link to="/dashboard" className="hover:text-red-hover active:text-red-hover">Dashboard</Link>
          </li>
          <li>
            <Link to="/signin" className="hover:text-red-hover active:text-red-hover">SignIn</Link>
          </li>
          <li>
            <button onClick={handleLogout}>Logout</button>
          </li>
        </ul>
      </nav>
    </>
  );
};

export default MainNavigation;
