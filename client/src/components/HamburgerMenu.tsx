import { NavLink } from "react-router-dom";

const HamburgerMenu: React.FC<{
  openState: boolean;
  handleClose: () => void;
}> = ({ openState, handleClose }) => {
  return (
    <>
      <div
        className={`fixed top-0 left-1/2 transform -translate-x-1/2 w-full h-screen p-6 bg-charcoal-800 text-red-500 shadow-lg z-50 overflow-auto ${
          openState ? "block" : "hidden"
        }`}
      >
        <div className="grid grid-cols-12">
          <ul className="col-start-3 col-end-11 flex flex-col items-center space-y-8 overflow-auto mt-32 mb-32">
            <li>
              <NavLink to="#">Movies</NavLink>
            </li>
            <li>
              <NavLink to="#">Cart</NavLink>
            </li>
            <li>
              <NavLink to="#">Account</NavLink>
            </li>
            <li>
              <NavLink to="#">Dashboard</NavLink>
            </li>
            <li>
              <NavLink to="signin">Sign in</NavLink>
            </li>
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
