import { StoreItem } from "@/types/Store";
import { useNavigate } from "react-router-dom";

const AdminStoreCard: React.FC<{ store: StoreItem }> = ({
  store,
}: {
  store: StoreItem;
}) => {
  const navigate = useNavigate();

  const handleDetailsClick = () => {
    navigate(`${store.id}`);
  };

  return (
    <>
      <div className="grid grid-cols-6 text-center items-center h-20 w-full text-charcoal-800 text-xs bg-gray-200 dark:bg-gray-900 hover:bg-gray-300 dark:text-gray-200 dark:border-gray-200 dark:hover:bg-gray-800 cursor-pointer my-0.25">
        <p>#{store.id}</p>
        <p className="col-span-2 line-clamp-3">
          {store.address.city.country.name}, {store.address.district},{" "}
          {store.address.city.name}
          {store.address.address}
        </p>
        <p className="col-span-2 line-clamp-3">
          {store.managerStaff.firstName} {store.managerStaff.lastName}
        </p>

        <div className="flex flex-col justify-around items-center">
          <button
            className="bg-green-500 hover:bg-green-800 text-charcoal-800 cursor-pointer p-1 w-12 m-1 rounded-sm"
            onClick={handleDetailsClick}
          >
            Details
          </button>
          <button className="bg-red-400 hover:bg-red-500 text-charcoal-800 cursor-pointer p-1 w-12 m-1 rounded-sm">
            Delete
          </button>
        </div>
      </div>
    </>
  );
};

export default AdminStoreCard;
