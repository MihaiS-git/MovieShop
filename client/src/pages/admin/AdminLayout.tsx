import AdminNavbarDrawer from "@/components/admin/AdminNavbarDrawer";
import PageContent from "@/PageContent";
import { Outlet } from "react-router-dom";
import { FaArrowRightArrowLeft } from "react-icons/fa6";
import { useEffect, useState } from "react";

const AdminLayout = () => {
  const [isAdminMenuOpen, setIsAdminMenuOpen] = useState(true);
  const [isSmallerScreen, setIsSmallerScreen] = useState(false);

  useEffect(() => {
    const handleResize = () => {
      setIsSmallerScreen(window.innerWidth < 1024);
    };

    handleResize();

    window.addEventListener("resize", handleResize);
  }, []);

  const toggleAdminMenu = () => {
    if (isSmallerScreen) {
      setIsAdminMenuOpen(!isAdminMenuOpen);
    }
  };

  return (
    <PageContent className="min-h-screen w-full pt-16 xl:pt-20 lg:grid lg:grid-cols-12">
      {/* Sidebar Column */}
      {isAdminMenuOpen && (
        <div className="hidden lg:block lg:col-span-2">
          <AdminNavbarDrawer toggleAdminMenu={toggleAdminMenu} />
        </div>
      )}

      {/* Main Content Column */}
      <div className="col-span-10 w-full h-full">
        <button
          onClick={toggleAdminMenu}
          className="lg:hidden fixed top-17 left-2 z-50 bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 p-2 rounded-4xl"
        >
          <FaArrowRightArrowLeft />
        </button>

        {/* Render Sidebar on small screens as drawer */}
        <div
          className={`lg:hidden fixed inset-0 z-40 transition-all duration-500 ${
            isAdminMenuOpen ? "pointer-events-auto" : "pointer-events-none"
          }`}
        >
          {/* Overlay */}
          <div
            className={`absolute inset-0 bg-charcoal-800 bg-opacity-50 transition-opacity duration-500 ${
              isAdminMenuOpen ? "opacity-100" : "opacity-0"
            }`}
            onClick={toggleAdminMenu}
          />

          {/* Sliding Drawer */}
          <div
            className={`fixed top-0 left-0 h-full w-full bg-charcoal-800 p-4 pt-16 overflow-y-auto z-50
      transform transition-transform duration-500 ease-in-out ${
        isAdminMenuOpen ? "translate-x-0" : "-translate-x-full"
      }`}
          >
            <AdminNavbarDrawer toggleAdminMenu={toggleAdminMenu} />
          </div>
        </div>

        <Outlet />
      </div>
    </PageContent>
  );
};

export default AdminLayout;
