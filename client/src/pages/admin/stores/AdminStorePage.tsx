import AdminStoreInventoriesSection from "@/components/admin/stores/AdminStoreInventoriesSection";
import EditStoreModal from "@/components/admin/stores/EditStoreModal";
import {
  useDeleteStoreByIdMutation,
  useGetStoreByIdQuery,
} from "@/features/stores/storeApi";
import PageContent from "@/PageContent";
import { formatDate } from "@/util/formatDate";
import { useState } from "react";
import { useParams } from "react-router-dom";

const AdminStorePage = () => {
  const { id } = useParams();
  const storeId = Number(id);

  const {
    data: store,
    isLoading,
    error,
    refetch,
  } = useGetStoreByIdQuery(storeId, {
    refetchOnMountOrArgChange: true,
  });

  const [showInventory, setShowInventory] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);

  const [deleteStore] = useDeleteStoreByIdMutation();

  const handleDeleteClick = async (storeId: number) => {
    try {
      await deleteStore(storeId);
      alert("Store successfully deleted.");
    } catch (err: unknown) {
      console.error("Failed to delete store: ", err);
    }
  };

  const handleInventoryClick = async () => {
    setShowInventory((prev) => !prev);
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading store data.</p>;

  const managerStaff = store?.managerStaff;
  const address = store?.address;

  return (
    <PageContent className="flex flex-col items-center justify-items-center 2xl:justify-items-start pt-4 w-full min-h-screen pb-12">
      <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 p-2 mb-4 rounded-2xl">
        Store Details
      </h1>
      <div className="flex flex-col items-center 2xl:items-start justify-evenly gap-2 2xl:grid 2xl:grid-cols-2 mb-2 w-full sm:w-8/12 lg:w-1/2 2xl:w-11/12 bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600 text-xs md:text-sm">
        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
          <div className="flex flex-row justify-between">
            <h2 className="text-xs md:text-sm pb-1">Store Manager</h2>
            <h2 className="font-bold text-xs md:text-sm pb-1">
              {managerStaff?.firstName} {managerStaff?.lastName}
            </h2>
          </div>
          <hr />
          <h3 className="font-bold text-xs md:text-sm pt-1 pb-1">Contact</h3>
          <div className="bg-gray-100 dark:bg-gray-700 p-2 shadow-sm">
            <div className="flex justify-between">
              <span className="font-semibold">E-mail:</span>
              <span>{managerStaff?.email}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">Phone:</span>
              <span>{managerStaff?.address.phone}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">Address:</span>
              <span>{managerStaff?.address.address}</span>
            </div>
            {managerStaff?.address.address2 && (
              <div className="flex justify-between">
                <span className="font-semibold">Address 2:</span>
                <span>{managerStaff?.address.address2}</span>
              </div>
            )}
            <div className="flex justify-between">
              <span className="font-semibold">Country:</span>
              <span>{managerStaff?.address.city.country.name}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">City:</span>
              <span>{managerStaff?.address.city.name}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">District:</span>
              <span>{managerStaff?.address.district}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">Postal Code:</span>
              <span>{managerStaff?.address.postalCode}</span>
            </div>
          </div>
        </div>

        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
          <div className="flex flex-row justify-between">
            <h2 className="text-xl md:text-sm pb-1">Store</h2>
            <h2 className="font-bold text-xs md:text-sm pb-1">
              #{store?.id}{" "}
              <span className="text-xs font-light">
                ({formatDate(store!.lastUpdate)})
              </span>
            </h2>
          </div>
          <hr />
          <h3 className="font-bold text-xs md:text-sm pt-1 pb-1">Contact</h3>
          <div className="bg-gray-100 dark:bg-gray-700 p-2 shadow-sm">
            <div className="flex justify-between">
              <span className="font-semibold">Phone: </span>
              <span>{address?.phone}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">Address:</span>
              <span> {address?.address}</span>
            </div>
            {address?.address2 && (
              <div className="flex justify-between">
                <span className="font-semibold">Address2:</span>
                <span> {address?.address2}</span>
              </div>
            )}
            <div className="flex justify-between">
              <span className="font-semibold">Country:</span>
              <span> {address?.city.country.name}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">City:</span>
              <span>{address?.city.name}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">District:</span>
              <span>{address?.district}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">Postal Code:</span>
              <span>{address?.postalCode}</span>
            </div>
          </div>
        </div>

        <div className="2xl:col-span-2 2xl:mx-auto">
          <button
            className="bg-gray-500 hover:bg-gray-600 text-charcoal-800 cursor-pointer p-1 m-1"
            onClick={() => handleInventoryClick()}
          >
            Inventory
          </button>
          <button
            className="bg-green-500 hover:bg-green-600 text-charcoal-800 cursor-pointer p-1 m-1"
            onClick={() => setShowEditModal(true)}
          >
            Edit Store
          </button>
          <button
            className="bg-red-500 hover:bg-red-600 text-charcoal-800 cursor-pointer p-1 m-1"
            onClick={() => handleDeleteClick(storeId)}
          >
            Delete Store
          </button>
        </div>
      </div>

      {showEditModal && address && (
        <EditStoreModal
          address={address}
          onClose={() => setShowEditModal(false)}
          refetchStore={refetch}
        />
      )}

      {showInventory && (
        <AdminStoreInventoriesSection storeId={storeId} />
      )}
    </PageContent>
  );
};

export default AdminStorePage;
