import { Link } from "react-router-dom";
import ThemeToggleButton from "./ThemeToggleButton";
import MainNavigation from "./MainNavigation";
import Hamburger from "./Hamburger";

const Header = () => {
  return (
    <header className="fixed flex justify-between items-center w-full p-4 xl:px-80 bg-gray-900 dark:bg-charcoal-800 text-white dark:text-white">
      <Link to="/" className="flex items-center w-full">
        <h1 className="text-red-500 dark:text-red-500 hover:text-red-hover text-xl lg:text-2xl font-extrabold">
          Movie Shop
        </h1>
      </Link>
      <MainNavigation />
      <ThemeToggleButton />
      <Hamburger />
    </header>
  );
};

export default Header;
