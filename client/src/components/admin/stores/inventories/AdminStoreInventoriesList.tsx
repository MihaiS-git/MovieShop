import { InventoryItem } from "@/types/Inventory";
import AdminInventoryCard from "./AdminInventoryCard";

const AdminStoreInventoriesList = ({inventories}: {inventories?: InventoryItem[]}) => {
  return (
    <div className="flex flex-col gap-1 w-full">
      {inventories ? (
        <ul>
          {inventories.map((inventory: InventoryItem) => (
            <li key={inventory.id}>
              <AdminInventoryCard inventory={inventory}/>
            </li>
          ))}
        </ul>
      ) : (
        <p>No inventories found for the selected store.</p>
      )}
    </div>
  );
};

export default AdminStoreInventoriesList;
