import { UserDetails } from "@/types/User";
import { formatDate } from "@/util/formatDate";
import { useEffect, useState } from "react";

type Props = {
  user: UserDetails;
};

const UserInfoCard = ({ user }: Props) => {
  const [createdAt, setCreatedAt] = useState("");
  const [lastUpdate, setLastUpdate] = useState("");

  useEffect(() => {
    setCreatedAt(formatDate(user?.createAt));
    setLastUpdate(formatDate(user?.lastUpdate));
  }, [user]);

  return (
    <div className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 rounded-lg p-4">
      <h2 className="text-xl font-semibold text-gold-500 py-4">General Info</h2>
      <ul className="flex flex-col justify-left items-baseline px-4 my-1 bg-gray-300 text-charcoal-800 rounded-sm gap-2 p-2">
        <li>User ID: #{user?.id}</li>
        <li>Role: {user?.role}</li>
        <li>Store ID: #{user?.storeId || "N/A"}</li>
        <li>Account Non Expired: {String(user?.accountNonExpired)}</li>
        <li>Account Non Locked: {String(user?.accountNonLocked)}</li>
        <li>Credentials Non Expired: {String(user?.credentialsNonExpired)}</li>
        <li>Enabled: {String(user?.enabled)}</li>
        <li>Created at: {createdAt}</li>
        <li>Last Update: {lastUpdate}</li>
      </ul>
    </div>
  );
};

export default UserInfoCard;
