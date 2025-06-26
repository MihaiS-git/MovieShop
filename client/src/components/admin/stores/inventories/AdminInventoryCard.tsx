import { InventoryItem } from "@/types/Inventory";
import { formatCurrency } from "@/util/formatCurrency";
import { useNavigate } from "react-router-dom";

const AdminInventoryCard: React.FC<{ inventory: InventoryItem }> = ({
  inventory,
}: {
  inventory: InventoryItem;
}) => {
  const navigate = useNavigate();

  const handleDetailsClick = () => {
    navigate(`/inventory/${inventory.id}`);
  };

  return (
      <div className="grid grid-cols-8 items-center text-center h-20 w-full text-charcoal-800 text-xs bg-gray-200 dark:bg-gray-900 hover:bg-gray-300 dark:text-gray-200 dark:border-gray-200 dark:hover:bg-gray-800 cursor-pointer my-0.25">
        <p>#{inventory.id}</p>
        <p>#{inventory.storeId}</p>
        <p className="col-span-2">{inventory.film.title}</p>
        <p>{inventory.film.releaseYear}</p>
        <p>{formatCurrency(inventory.film.rentalRate)}</p>
        <p>{formatCurrency(inventory.film.replacementCost)}</p>

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
  );
};

export default AdminInventoryCard;
