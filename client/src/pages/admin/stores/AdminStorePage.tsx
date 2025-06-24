import { useGetStoreByIdQuery } from "@/features/stores/storeApi";
import PageContent from "@/PageContent";
import { Address } from "@/types/Address";
import { Inventory } from "@/types/Inventory";
import { UserDetails } from "@/types/User";
import { formatDate } from "@/util/formatDate";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const AdminStorePage = () => {
  const { id } = useParams();
  const storeId = Number(id);

  console.log("STORE ID: ", storeId);

  const { data, isLoading, error } = useGetStoreByIdQuery(storeId, {
    refetchOnMountOrArgChange: true,
  });

  const [managerStaff, setManagerStaff] = useState<UserDetails>();
  const [address, setAddress] = useState<Address>();
  const [inventories, setInventories] = useState<Inventory[]>();

  useEffect(() => {
    if (data) {
      setManagerStaff(data.managerStaff);
      setAddress(data.address);
      setInventories(data.inventories);
    }
  }, [data]);

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading store data.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-items-center 2xl:justify-items-start pt-4 w-full min-h-screen 2xl:pb-56">
      <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 p-2 mb-4 rounded-2xl">
        Store Details
      </h1>
      <div className="flex flex-col items-center 2xl:items-start justify-evenly gap-4 2xl:flex-row mb-24 w-full sm:w-8/12 lg:w-1/2 2xl:w-11/12 bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
        <p>{data?.id}</p>
        <p>{formatDate(data!.lastUpdate)}</p>
        <div>
          <h2>Store Manager</h2>
          <p>First Name: {managerStaff?.firstName}</p>
          <p>Last Name:{managerStaff?.lastName}</p>

          <h3>Manager Contact</h3>
          <p>E-mail: {managerStaff?.email}</p>
          <p>Phone: {managerStaff?.address.phone}</p>

          <p>Address: {managerStaff?.address.address}</p>
          <p>{managerStaff?.address.address2}</p>
          <p>Country: {managerStaff?.address.city.country.name}</p>
          <p>City: {managerStaff?.address.city.name}</p>
          <p>District: {managerStaff?.address.district}</p>
          <p>Postal Code: {managerStaff?.address.postalCode}</p>
        </div>
        <div>
          <h2>Store Address</h2>
          <p>Phone: {address?.phone}</p>
          <p>Address: {address?.address}</p>
          <p>{address?.address2}</p>
          <p>Country: {address?.city.country.name}</p>
          <p>City: {address?.city.name}</p>
          <p>District: {address?.district}</p>
          <p>Postal Code: {address?.postalCode}</p>
        </div>
        <div>
          <h2>Inventories</h2>
          <ul>
            {inventories?.map((inventory) => (
              <li key={inventory.id}>
                {inventory.id} | {inventory.storeId} | {inventory.filmId} |{" "}
                {formatDate(inventory.lastUpdate)}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </PageContent>
  );
};

export default AdminStorePage;
