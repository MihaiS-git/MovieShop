import { NavLink } from "react-router-dom";
import LogoutButton from "./LogoutButton";
import { useSelector } from "react-redux";
import { RootState } from "../app/store";

interface HamburgerMenuProps {
  openState: boolean;
  handleClose: () => void;
}

const HamburgerMenu: React.FC<HamburgerMenuProps> = ({ openState, handleClose }) => {
  const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated);

  return (
    <>
      <div
        className={`fixed top-0 left-1/2 transform -translate-x-1/2 w-full h-screen p-6 bg-charcoal-800 text-red-500 shadow-lg z-50 overflow-auto 
        transition-all duration-500 ease-in-out ${openState ? "pointer-events-auto opacity-100" : "pointer-events-none opacity-0"}`}
      >
        <div className="grid grid-cols-12">
          <ul className="col-start-3 col-end-11 flex flex-col items-center space-y-8 overflow-auto mt-32 mb-32">
            <li>
              <NavLink to="/" onClick={handleClose}>
                Home
              </NavLink>
            </li>
            <li>
              <NavLink to="/movies" onClick={handleClose}>
                Movies
              </NavLink>
            </li>
            <li>
              <NavLink to="#" onClick={handleClose}>
                Cart
              </NavLink>
            </li>
            <li>
              <NavLink to="/account" onClick={handleClose}>
                Account
              </NavLink>
            </li>
            <li>
              <NavLink to="/admin" onClick={handleClose}>
                Admin
              </NavLink>
            </li>
            {!isAuthenticated ? (
              <li>
                <NavLink to="signin" onClick={handleClose}>
                  SignIn
                </NavLink>
              </li>
            ) : (
              <li onClick={handleClose}>
                <LogoutButton />
              </li>
            )}
          </ul>
          <div className="col-span-1"></div>
          <div className="col-start-12">
            <button
              className="ps-2 pe-2 text-md text-charcoal-800 bg-red-500 rounded-md"
              onClick={handleClose}
            >
              X
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default HamburgerMenu;
