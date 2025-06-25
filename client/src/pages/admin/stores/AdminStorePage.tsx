import { useGetStoreByIdQuery } from "@/features/stores/storeApi";
import PageContent from "@/PageContent";
import { Address } from "@/types/Address";
import { UserDetails } from "@/types/User";
import { formatDate } from "@/util/formatDate";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

const AdminStorePage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const storeId = Number(id);

  const { data, isLoading, error } = useGetStoreByIdQuery(storeId, {
    refetchOnMountOrArgChange: true,
  });

  const [managerStaff, setManagerStaff] = useState<UserDetails>();
  const [address, setAddress] = useState<Address>();

  useEffect(() => {
    if (data) {
      setManagerStaff(data.managerStaff);
      setAddress(data.address);
    }
  }, [data]);

  const handleInventoryClick = () => {
    navigate("inventory");
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading store data.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-items-center 2xl:justify-items-start pt-4 w-full min-h-screen 2xl:pb-56">
      <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 p-2 mb-4 rounded-2xl">
        Store Details
      </h1>
      <div className="flex flex-col items-center 2xl:items-start justify-evenly gap-4 2xl:grid 2xl:grid-cols-2 mb-24 w-full sm:w-8/12 lg:w-1/2 2xl:w-11/12 bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600 text-xs md:text-base">

        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-8 border border-charcoal-800 dark:border-gray-600">
          <div className="flex flex-row justify-between">
            <h2 className="text-sm md:text-xl pb-4">Store Manager</h2>
            <h2 className="font-bold text-sm md:text-xl pb-4">
              {managerStaff?.firstName} {managerStaff?.lastName}
            </h2>
          </div>
          <hr />
          <h3 className="font-bold text-xs md:text-base pt-4 pb-1">Contact</h3>
          <div className="bg-gray-100 dark:bg-gray-700 rounded-xl p-4 shadow-sm">
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

        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-8 border border-charcoal-800 dark:border-gray-600">
          <div className="flex flex-row justify-between">
            <h2 className="text-sm md:text-xl pb-4">Store</h2>
            <h2 className="font-bold text-sm md:text-xl pb-4">
              #{data?.id}{" "}
              <span className="text-xs font-light">
                ({formatDate(data!.lastUpdate)})
              </span>
            </h2>
          </div>
          <hr />
          <h3 className="font-bold text-xs md:text-base pt-4 pb-1">Contact</h3>
          <div className="bg-gray-100 dark:bg-gray-700 rounded-xl p-4 shadow-sm">
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
            className="bg-gray-500 hover:bg-gray-600 text-charcoal-800 cursor-pointer p-1.5 m-1 rounded-sm"
            onClick={handleInventoryClick}
          >
            Inventory
          </button>
          <button
            className="bg-green-500 hover:bg-green-600 text-charcoal-800 cursor-pointer p-1.5 m-1 rounded-sm"
            onClick={handleInventoryClick}
          >
            Edit Store
          </button>
          <button
            className="bg-red-500 hover:bg-red-600 text-charcoal-800 cursor-pointer p-1.5 m-1 rounded-sm"
            onClick={handleInventoryClick}
          >
            Delete Store
          </button>
        </div>
      </div>
    </PageContent>
  );
};

export default AdminStorePage;
