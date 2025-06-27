import { NavLink } from "react-router-dom";

interface AdminNavDrawerProps {
  toggleAdminMenu: () => void;
}

const AdminNavbarDrawer: React.FC<AdminNavDrawerProps> = ({
  toggleAdminMenu,
}) => {
  return (
    <aside className="h-full w-full bg-charcoal-800 text-gray-400 text-base p-4 py-24 z-90">
      <ul className="flex flex-col items-left gap-4">
        <li onClick={toggleAdminMenu}>
          <div className="flex flex-col ">
            <h1 className="pb-2 text-xl">Movies</h1>
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-red-500 font-bold"
                  : "text-gray-300 hover:text-red-500"
              }
              to="/admin/movies/edit"
            >
              Movies List
            </NavLink>
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-red-500 font-bold"
                  : "text-gray-300 hover:text-red-500"
              }
              to="/admin/movies/add"
            >
              Add Movie
            </NavLink>
            <hr className="border-t border-gray-400 my-2" />
          </div>
        </li>

        <li onClick={toggleAdminMenu}>
          <div className="flex flex-col ">
            <h1 className="pb-2 text-xl">Users</h1>
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-red-500 font-bold"
                  : "text-gray-300 hover:text-red-500"
              }
              to="/admin/users"
            >
              Users List
            </NavLink>
            <hr className="border-t border-gray-400 my-2" />
          </div>
        </li>

        <li onClick={toggleAdminMenu}>
          <div className="flex flex-col ">
            <h1 className="pb-2 text-xl">Stores</h1>
            <NavLink
              className={({ isActive }) =>
                isActive
                  ? "text-red-500 font-bold"
                  : "text-gray-300 hover:text-red-500"
              }
              to="/admin/stores"
            >
              Stores List
            </NavLink>

            <hr className="border-t border-gray-400 my-2" />
          </div>
        </li>
      </ul>
    </aside>
  );
};

export default AdminNavbarDrawer;
