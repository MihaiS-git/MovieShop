import { UserItem } from "@/types/User";
import { useNavigate } from "react-router-dom";

const AdminMobileUserCard: React.FC<{
  user: UserItem;
  handleDeleteClick: (value: number) => void;
}> = ({ user, handleDeleteClick }) => {
  const navigate = useNavigate();

  const handleEditClick = () => {
    navigate(`${user.id}`);
  };

  return (
    <div className="grid grid-cols-7 items-center text-center h-20 w-full text-charcoal-800 text-xs bg-gray-200 dark:bg-gray-900 hover:bg-gray-300 dark:text-gray-200 dark:border-gray-200 dark:hover:bg-gray-800 cursor-pointer my-0.25">
      <p>{user.id}</p>
      <p className="col-span-2 truncate max-w-full overflow-hidden text-ellipsis">
        {user.email}
      </p>
      <p className="truncate max-w-full overflow-hidden text-ellipsis">
        {user.role}
      </p>
      <p className="truncate max-w-full overflow-hidden text-ellipsis">
        {user.firstName}
      </p>
      <p className="truncate max-w-full overflow-hidden text-ellipsis">
        {user.lastName}
      </p>
      <div className="flex flex-col justify-around items-center p-1">
        <button
          className="bg-green-500 hover:bg-green-800 text-charcoal-800 cursor-pointer p-1 w-12 m-1 rounded-sm"
          onClick={handleEditClick}
        >
          Edit
        </button>
        <button
          className="bg-red-400 hover:bg-red-500 text-charcoal-800 cursor-pointer p-1 w-12 m-1 rounded-sm"
          onClick={() => handleDeleteClick(user.id)}
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default AdminMobileUserCard;
