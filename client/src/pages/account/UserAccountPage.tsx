import { useGetUserByEmailQuery } from "@/features/users/userApi";
import PageContent from "@/PageContent";
import UserInfoCard from "@/components/user/UserInfoCard";
import UserNameForm from "./UserNameForm";
import AddressForm from "./AddressForm";

const UserAccountPage = () => {
  const authRaw = localStorage.getItem("auth");
  const userData = authRaw ? JSON.parse(authRaw)?.user : null;
  const email = userData?.email;

  const {
    data: user,
    isLoading,
    error,
  } = useGetUserByEmailQuery(email, { refetchOnMountOrArgChange: true });

  if (isLoading) return <p>Loading...</p>;
  if (error) {
    const message =
      "status" in error ? JSON.stringify(error.data) : "Unknown error";
    return <p>Error: {message}</p>;
  }

  return (
    <PageContent className="flex flex-col text-center min-h-screen items-center py-24 text-sm">
      <h1 className="font-bold text-2xl mb-4 text-charcoal-800 dark:text-gold-500 py-4">
        User Account
      </h1>
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {user && <UserInfoCard user={user} />}
        {user && <UserNameForm user={user} />}
        {user && <AddressForm user={user} />}
      </div>
    </PageContent>
  );
};

export default UserAccountPage;
