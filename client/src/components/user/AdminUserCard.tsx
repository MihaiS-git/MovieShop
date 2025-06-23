import { UserItem } from "@/types/User";
import { formatDate } from "@/util/formatDate";
import { useNavigate } from "react-router-dom";

const AdminUserCard: React.FC<{
  user: UserItem;
  handleDeleteClick: (value: number) => void;
}> = ({user, handleDeleteClick}) => {
  const navigate = useNavigate();

  const handleEditClick = () => {
    navigate(`${user.id}`);
  };

  return (
    <div className="grid grid-cols-12 items-center text-center h-20 w-full text-charcoal-800 text-xs bg-gray-200 dark:bg-gray-900 hover:bg-gray-300 dark:text-gray-200 dark:border-gray-200 dark:hover:bg-gray-800 cursor-pointer my-0.25">
      <p>{user.id}</p>
      <p className="col-span-2 truncate max-w-full overflow-hidden text-ellipsis">
        {user.email}
      </p>
      <p>{user.role}</p>
      <p>{user.firstName}</p>
      <p>{user.lastName}</p>
      <p>{String(user.accountNonExpired)}</p>
      <p>{String(user.accountNonLocked)}</p>
      <p>{String(user.credentialsNonExpired)}</p>
      <p>{String(user.enabled)}</p>
      <p>
        {formatDate(user.createAt)}
        <br />
        {formatDate(user.lastUpdate)}
      </p>
      <div className="flex flex-col justify-around items-center">
        <button
          className="bg-green-500 hover:bg-green-800 text-charcoal-800 cursor-pointer p-1 w-15 m-1 rounded-sm"
          onClick={handleEditClick}
        >
          Edit
        </button>
        <button
          className="bg-red-400 hover:bg-red-500 text-charcoal-800 cursor-pointer p-1 w-15 m-1 rounded-sm"
          onClick={() => handleDeleteClick(user.id)}
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default AdminUserCard;
