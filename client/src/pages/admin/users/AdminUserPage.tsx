import {
  useGetUserByIdQuery,
  useUpdateUserAccountMutation,
} from "@/features/users/userApi";
import PageContent from "@/PageContent";
import { formatDate } from "@/util/formatDate";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const AdminUserPage = () => {
  const { id } = useParams();
  const userId = Number(id);

  const [userAccountNonExpired, setUserAccountNonExpired] =
    useState<boolean>(false);
  const [userAccountNonLocked, setUserAccountNonLocked] =
    useState<boolean>(false);
  const [userCredentialsNonExpired, setUserCredentialsNonExpired] =
    useState<boolean>(false);
  const [userEnabled, setUserEnabled] = useState<boolean>(false);
  const [rentals, setRentals] = useState<number[]>([]);
  const [customerPayments, setCustomerPayments] = useState<number[]>([]);
  const [staffPayments, setStaffPayments] = useState<number[]>([]);
  const [showEditModal, setShowEditModal] = useState(false);

  const { data, isLoading, error, refetch } = useGetUserByIdQuery(userId, {
    refetchOnMountOrArgChange: true,
  });

  const [updateUser] = useUpdateUserAccountMutation();

  const user = data;

  useEffect(() => {
    if (!user) return;
    setUserAccountNonExpired(user.accountNonExpired);
    setUserAccountNonLocked(user.accountNonLocked);
    setUserCredentialsNonExpired(user.credentialsNonExpired);
    setUserEnabled(user.enabled);
    setRentals(user.rentalIds);
    setCustomerPayments(user.customerPaymentIds);
    setStaffPayments(user.staffPaymentIds);
  }, [user]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await updateUser({
        id: user!.id,
        data: {
          accountNonExpired: userAccountNonExpired,
          accountNonLocked: userAccountNonLocked,
          credentialsNonExpired: userCredentialsNonExpired,
          enabled: userEnabled,
        },
      }).unwrap();

      refetch();
    } catch (err) {
      console.error("Failed to update user account settings:", err);
    }
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>An error occurred on user loading.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-items-center 2xl:justify-items-start pt-4 w-full min-h-screen pb-12">
      <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 p-2 mb-4 rounded-2xl">
        Edit User
      </h1>
      <div className="flex flex-col items-center 2xl:items-start justify-evenly gap-2 2xl:grid 2xl:grid-cols-2 mb-2 w-full sm:w-8/12 lg:w-1/2 2xl:w-11/12 bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600 text-xs md:text-sm">
        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
          <div className="flex flex-row justify-between">
            <h2 className="text-xs md:text-sm pb-1">{user?.role}</h2>
            <h2 className="font-bold text-xs md:text-sm pb-1">
              {user?.firstName} {user?.lastName}
            </h2>
          </div>
          <h3 className="font-bold text-xs md:text-sm pt-1 pb-1">
            User Information
          </h3>
          <div className="bg-gray-100 dark:bg-gray-700 p-2 shadow-sm">
            <div className="flex justify-between">
              <span className="font-semibold">User ID: </span>
              <span>#{user?.id}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">Email: </span>
              <span>{user?.email}</span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold">Store ID: </span>
              <span>#{user?.storeId}</span>
            </div>
            {user?.picture && (
              <div className="flex justify-between">
                <span className="font-semibold">Picture: </span>
                <span>{user?.picture}</span>
              </div>
            )}
          </div>
        </div>

        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
          <h3 className="font-bold text-xs md:text-sm pt-1 pb-1">
            User Address
          </h3>
          <div className="bg-gray-100 dark:bg-gray-700 p-2 shadow-sm">
            <div className="flex justify-between">
              <span className="font-semibold">Address: </span>
              <span>{user?.address.address}</span>
            </div>

            {user?.address.address2 && (
              <div className="flex justify-between">
                <span className="font-semibold">Address2: </span>
                <span>{user?.address.address2}</span>
              </div>
            )}

            <div className="flex justify-between">
              <span className="font-semibold">District: </span>
              <span>{user?.address.district}</span>
            </div>

            <div className="flex justify-between">
              <span className="font-semibold">City: </span>
              <span>{user?.address.city.name}</span>
            </div>

            <div className="flex justify-between">
              <span className="font-semibold">Country: </span>
              <span>{user?.address.city.country.name}</span>
            </div>

            <div className="flex justify-between">
              <span className="font-semibold">Phone: </span>
              <span>{user?.address.phone}</span>
            </div>

            <div className="flex justify-between">
              <span className="font-semibold">Postal Code: </span>{" "}
              <span>{user?.address.postalCode}</span>
            </div>
          </div>
        </div>

        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
          <h3 className="font-bold text-xs md:text-sm pt-1 pb-1">
            Account Settings
          </h3>
          <div className="bg-gray-100 dark:bg-gray-700 p-2 px-16 shadow-sm">
            <form onSubmit={handleSubmit} className="space-y-4 text-xs md:text-sm">
              <div className="flex flex-row justify-between">
                <label>
                  Account Non Expired
                </label>
                <input
                  type="checkbox"
                  name="userAccountNonExpired"
                  checked={userAccountNonExpired}
                  onChange={(e) => setUserAccountNonExpired(e.target.checked)}
                />
              </div>
              <div className="flex flex-row justify-between">
                <label>
                  Account Non Locked
                </label>
                <input
                  type="checkbox"
                  name="accountNonLocked"
                  checked={userAccountNonLocked}
                  onChange={(e) => setUserAccountNonLocked(e.target.checked)}
                />
              </div>
              <div className="flex flex-row justify-between">
                <label>
                  Credentials Non Expired
                </label>
                <input
                  type="checkbox"
                  name="credentialsNonExpired"
                  checked={userCredentialsNonExpired}
                  onChange={(e) =>
                    setUserCredentialsNonExpired(e.target.checked)
                  }
                />
              </div>
              <div className="flex flex-row justify-between">
                <label>Enabled</label>
                <input
                  type="checkbox"
                  name="Enabled"
                  checked={userEnabled}
                  onChange={(e) => setUserEnabled(e.target.checked)}
                />
              </div>

              <div className="w-full text-center">
                <button
                  type="submit"
                  className="bg-green-500 hover:bg-green-600 text-charcoal-800 cursor-pointer p-1 m-1"
                >
                  Save changes
                </button>
              </div>
            </form>
          </div>
        </div>

        <div className="flex-1 self-stretch bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
          <div className="flex justify-between">
            <span className="font-semibold">Created at: </span>
            <span>{formatDate(user!.createAt)}</span>
          </div>

          <div className="flex justify-between">
            <span className="font-semibold">Last update: </span>
            <span>{formatDate(user!.lastUpdate)}</span>
          </div>
        </div>

        <div className="flex flex-row items-center justify-center col-span-2">
          <button className="bg-green-500 hover:bg-green-600 text-charcoal-800 cursor-pointer p-1 m-1">
            Rentals
          </button>
          <button className="bg-green-500 hover:bg-green-600 text-charcoal-800 cursor-pointer p-1 m-1">
            Payments
          </button>
          <button
            className="bg-green-500 hover:bg-green-600 text-charcoal-800 cursor-pointer p-1 m-1"
            onClick={() => setShowEditModal(true)}
          >
            Edit Account
          </button>
        </div>
      </div>
    </PageContent>
  );
};

export default AdminUserPage;
