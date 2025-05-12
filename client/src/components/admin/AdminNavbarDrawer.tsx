import { NavLink } from "react-router-dom";

interface AdminNavDrawerProps {
    toggleAdminMenu: () => void;
}

const AdminNavbarDrawer: React.FC<AdminNavDrawerProps> = ({ toggleAdminMenu }) => {
    return (
        <aside className="h-full w-full bg-charcoal-800 text-gray-100 p-4 py-24 z-90">
            <ul className="flex flex-col items-center gap-8">
                <li onClick={toggleAdminMenu}><NavLink className={({isActive}) => isActive ? "text-red-500 font-bold" : "text-gray-300 hover:text-red-500"} to="/admin/movies">Movies</NavLink></li>
                <li onClick={toggleAdminMenu}><NavLink className={({isActive}) => isActive ? "text-red-500 font-bold" : "text-gray-300 hover:text-red-500"} to="/admin/users">Users</NavLink></li>
                <li onClick={toggleAdminMenu}><NavLink className={({isActive}) => isActive ? "text-red-500 font-bold" : "text-gray-300 hover:text-red-500"} to="/admin/orders">Orders</NavLink></li>
                <li onClick={toggleAdminMenu}><NavLink className={({isActive}) => isActive ? "text-red-500 font-bold" : "text-gray-300 hover:text-red-500"} to="/admin/reviews">Reviews</NavLink></li>
                <li onClick={toggleAdminMenu}><NavLink className={({isActive}) => isActive ? "text-red-500 font-bold" : "text-gray-300 hover:text-red-500"} to="/admin/dashboard">Dashboard</NavLink></li>
            </ul>
        </aside>
    );
};

export default AdminNavbarDrawer;