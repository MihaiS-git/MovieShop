import AdminStoresList from "@/components/admin/stores/AdminStoresList";
import PageContent from "@/PageContent";
import { useEffect } from "react";

const AdminStoresListPage = () => {
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  return (
    <PageContent className="flex flex-col items-center justify-center pt-4 pb-24 w-full">
        <AdminStoresList />
    </PageContent>
  );
};

export default AdminStoresListPage;
