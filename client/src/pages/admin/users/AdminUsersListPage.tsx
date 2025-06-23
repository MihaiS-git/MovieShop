import AdminUsersList from "@/components/user/AdminUsersList";
import { useDeleteUserByIdMutation } from "@/features/users/userApi";
import { usePaginatedUsers } from "@/hooks/usePaginatedUsers";
import PageContent from "@/PageContent";
import { useEffect } from "react";

const AdminUsersListPage = () => {
  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  const {
    totalPages,
    page,
    handleNextPage,
    handlePrevPage,
    onPageChange,
    hasMore,
    loadMore,
    orderBy,
    setOrderBy,
    roleFilter,
    setRoleFilter,
    enabledFilter,
    setEnabledFilter,
    accountNonExpiredFilter,
    setAccountNonExpiredFilter,
    accountNonLockedFilter,
    setAccountNonLockedFilter,
    credentialsNonExpiredFilter,
    setCredentialsNonExpiredFilter,
    searchFilter,
    handleSearchTermChange,
    userItems,
    setUserItems,
    isLoading,
    error,
    resetAllFilters,
  } = usePaginatedUsers();

  const [deleteUser] = useDeleteUserByIdMutation();

  const handleDelete = async (userId: number) => {
    try {
      await deleteUser(userId);
      setUserItems((prev) => prev.filter((u) => u.id !== userId));
    } catch (err: unknown) {
      console.error("Failed to delete user: ", err);
    }
  };

  if (isLoading && page === 1) return <p>Loading...</p>;
  if (error) return <p>Error loading users.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center pt-4 pb-24 w-full">
      <AdminUsersList
        userPagination={{
          totalPages,
          page,
          handleNextPage,
          handlePrevPage,
          onPageChange,
          hasMore,
          loadMore,
        }}
        userFilters={{
          orderBy,
          setOrderBy,
          roleFilter,
          setRoleFilter,
          enabledFilter,
          setEnabledFilter,
          accountNonExpiredFilter,
          setAccountNonExpiredFilter,
          accountNonLockedFilter,
          setAccountNonLockedFilter,
          credentialsNonExpiredFilter,
          setCredentialsNonExpiredFilter,
          searchFilter,
          handleSearchTermChange,
          resetAllFilters,
        }}
        users={userItems}
        handleDelete={handleDelete}
      />
    </PageContent>
  );
};

export default AdminUsersListPage;
