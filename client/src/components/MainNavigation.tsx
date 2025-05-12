import { Link, NavLink } from "react-router-dom";
import LogoutButton from "./LogoutButton";
import { useSelector } from "react-redux";
import { RootState } from "../app/store";

const MainNavigation = () => {
  const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated);
  
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
            <Link to="/admin" className="hover:text-red-hover active:text-red-hover">Admin</Link>
          </li>
          {!isAuthenticated ? (
              <li>
                <NavLink to="signin" className="hover:text-red-hover active:text-red-hover">
                  SignIn
                </NavLink>
              </li>
            ) : (
              <li>
                <LogoutButton />
              </li>
            )}
        </ul>
      </nav>
    </>
  );
};

export default MainNavigation;
