import ListItemsPagination from "@/components/ui/ListItemsPagination";
import { useDeleteUserByIdMutation, useGetUsersQuery } from "@/features/users/userApi";
import { useIsMobile } from "@/hooks/useIsMobile";
import PageContent from "@/PageContent";
import { ROLE, UserItem } from "@/types/User";
import { formatDate } from "@/util/formatDate";
import React from "react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const AdminUsers = () => {
  const navigate = useNavigate();
  const limit = 20;
  const isMobile = useIsMobile();
  const [userItems, setUserItems] = useState<UserItem[]>([]);
  const [page, setPage] = useState(1);
  const [orderBy, setOrderBy] = useState("None");
  const [roleFilter, setRoleFilter] = useState("All");
  const [searchFilter, setSearchFilter] = useState("");
  const [enabledFilter, setEnabledFilter] = useState("All");
  const [accountNonExpiredFilter, setAccountNonExpiredFilter] = useState("All");
  const [accountNonLockedFilter, setAccountNonLockedFilter] = useState("All");
  const [credentialsNonExpiredFilter, setCredentialsNonExpiredFilter] =
    useState("All");
  const [debounceSearchTerm, setDebounceSearchTerm] = useState(searchFilter);

  const { data, error, isLoading, isFetching } = useGetUsersQuery({
    page: page - 1,
    limit,
    orderBy: orderBy == "None" ? undefined : orderBy,
    roleFilter: roleFilter === "All" ? undefined : roleFilter,
    searchFilter,
    enabledFilter: enabledFilter === "All" ? undefined : enabledFilter,
    accountNonExpiredFilter: accountNonExpiredFilter === "All" ? undefined : accountNonExpiredFilter,
    accountNonLockedFilter: accountNonLockedFilter === "All" ? undefined : accountNonLockedFilter,
    credentialsNonExpiredFilter: credentialsNonExpiredFilter === "All" ? undefined : credentialsNonExpiredFilter,
  });

  const totalCount = data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / limit);
  const hasMore = page < totalPages;

  useEffect(() => {
    const timeout = setTimeout(() => {
      setDebounceSearchTerm(searchFilter);
    }, 300);

    return () => clearTimeout(timeout);
  }, [searchFilter]);

  useEffect(() => {
    setPage(1);
  }, [
    orderBy,
    roleFilter,
    searchFilter,
    enabledFilter,
    accountNonExpiredFilter,
    accountNonLockedFilter,
    credentialsNonExpiredFilter,
    debounceSearchTerm,
  ]);

  useEffect(() => {
    if (!data || !data.users) return;

    setUserItems((prev) => {
      if (page === 1) return data.users;

      const prevIds = new Set(prev.map((u) => u.id));
      const newUsers = data.users.filter((u) => !prevIds.has(u.id));

      if (newUsers.length === 0) return prev;

      return isMobile ? [...prev, ...newUsers] : [...newUsers];
    });
  }, [data, isMobile, page]);

  const resetAllFilters = () => {
    setOrderBy("");
    setRoleFilter("All");
    setSearchFilter("");
    setEnabledFilter("All");
    setAccountNonExpiredFilter("All");
    setAccountNonLockedFilter("All");
    setCredentialsNonExpiredFilter("All");
  };

  const handleNextPage = () => {
    if (page < totalPages) {
      setPage((prev) => prev + 1);
    }
  };

  const handlePrevPage = () => {
    if (page > 1) {
      setPage((prev) => prev - 1);
    }
  };

  const onPageChange = (pageNo: number) => {
    setPage(pageNo);
  };

  const loadMore = () => {
    if (page === 1 && (isLoading || isFetching)) return;

    if (!isLoading && !isFetching && hasMore) {
      setPage((prev) => prev + 1);
    }
  };

  const handleSearchTermChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchFilter(e.target.value);
    setPage(1);
  };

  const handleEditClick = (id: number) => {
    navigate(`${id}`);
  };

  const [deleteUser] = useDeleteUserByIdMutation();
  
  const handleDeleteClick = async (userId: number) => {
    try{
      await deleteUser(userId);
      setUserItems((prev) => prev.filter((u) => u.id !== userId));
    } catch (err: unknown){
      console.error("Failed to delete user: ", err);
    }
  };

  const usersPagination = {
    totalPages,
    page,
    handleNextPage,
    handlePrevPage,
    onPageChange,
    hasMore,
    loadMore,
  };

  /*   const userFilters = {
    orderBy,
    setOrderBy,
    roleFilter,
    setRoleFilter,
    searchFilter,
    setSearchFilter,
    enabledFilter,
    setEnabledFilter,
    accountNonExpiredFilter,
    setAccountNonExpiredFilter,
    accountNonLockedFilter,
    setAccountNonLockedFilter,
    credentialsNonExpiredFilter,
    setCredentialsNonExpiredFilter,
  }; */

  if (isLoading || isFetching) return <p>Loading...</p>;
  if (error) return <p>Error loading users.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center pt-4 pb-24 px-4 w-full">
      <div className="w-full h-full text-red-500 dark:text-charcoal-800 py-4 flex flex-col items-center">
        <h1 className="bg-charcoal-800 dark:bg-red-500 text-base lg:text-lg text-center p-2 rounded-2xl w-50">
          Users
        </h1>
      </div>

      <div className="w-full mb-4 rounded-lg bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-sm">
        <div className="w-full grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 xl:grid-cols-8 2xl:grix-cols-10 p-2 gap-3 sm:gap-x-5 md:gap-x-10 md:px-12 lg:gap-x-2 lg:px-2">
          <div className="flex flex-row w-full items-center justify-start cols-span-1 xl:col-span-2 mx-auto">
            <label htmlFor="searchUser" className="w-full sm:w-15 text-start">
              Search:{" "}
            </label>
            <input
              type="text"
              id="searchUser"
              name="searchUser"
              value={searchFilter}
              onChange={handleSearchTermChange}
              className="w-full bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 ps-2"
              placeholder="Enter search term..."
            />
          </div>

          <div className="flex flex-row w-full items-center justify-start col-span-1 xl:col-span-2 mx-auto">
            <label htmlFor="orderBy" className="w-full sm:w-15 text-start">
              Sort by:
            </label>
            <select
              name="orderBy"
              id="orderBy"
              value={orderBy}
              className="w-full bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1"
              onChange={(e) => setOrderBy(e.target.value)}
            >
              <option value="None">None</option>
              <option value="email_asc">Email Ascending</option>
              <option value="email_desc">Email Descending</option>
              <option value="firstName_asc">First Name Ascending</option>
              <option value="firstName_desc">First Name Descending</option>
              <option value="lastName_asc">Last Name Ascending</option>
              <option value="lastName_desc">Last Name Descending</option>
            </select>
          </div>

          <div className="flex flex-row w-full items-center justify-start col-span-1 xl:col-span-2 mx-auto">
            <label htmlFor="roleFilter" className="w-full sm:w-15 text-start">
              Role:{" "}
            </label>
            <select
              name="roleFilter"
              id="roleFilter"
              value={roleFilter}
              className="w-full bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1"
              onChange={(e) => setRoleFilter(e.target.value)}
            >
              <option value="All">All</option>
              {ROLE.map((role) => (
                <option key={role} value={role}>
                  {role}
                </option>
              ))}
            </select>
          </div>

          <div className="flex flex-row w-full items-center justify-start col-span-1 xl:col-span-2 mx-auto">
            <label
              htmlFor="enabledFilter"
              className="w-full sm:w-15 text-start"
            >
              Enabled:{" "}
            </label>
            <select
              name="enabledFilter"
              id="enabledFilter"
              value={enabledFilter}
              className="w-full bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1"
              onChange={(e) => setEnabledFilter(e.target.value)}
            >
              <option value="All">All</option>
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
          </div>

          <div className="flex flex-row w-full items-center justify-start col-span-1 xl:col-span-2 mx-auto">
            <label
              htmlFor="accountNonExpiredFilter"
              className="w-full text-start"
            >
              Account Non Expired:{" "}
            </label>
            <select
              name="accountNonExpiredFilter"
              id="accountNonExpiredFilter"
              value={accountNonExpiredFilter}
              className="w-full bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1"
              onChange={(e) => setAccountNonExpiredFilter(e.target.value)}
            >
              <option value="All">All</option>
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
          </div>

          <div className="flex flex-row w-full items-center justify-start col-span-1 xl:col-span-2 mx-auto">
            <label
              htmlFor="accountNonLockedFilter"
              className="w-full sm:w-15  text-start"
            >
              Account Non Locked:{" "}
            </label>
            <select
              name="accountNonLockedFilter"
              id="accountNonLockedFilter"
              value={accountNonLockedFilter}
              className="w-full bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1"
              onChange={(e) => setAccountNonLockedFilter(e.target.value)}
            >
              <option value="All">All</option>
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
          </div>

          <div className="flex flex-row w-full items-center justify-between lg:justify-start col-span-1 xl:col-span-2 mx-auto">
            <label
              htmlFor="credentialsNonExpiredFilter"
              className="w-full text-start"
            >
              Credentials Non Expired:{" "}
            </label>
            <select
              name="credentialsNonExpiredFilter"
              id="credentialsNonExpiredFilter"
              value={credentialsNonExpiredFilter}
              className="w-full bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1"
              onChange={(e) => setCredentialsNonExpiredFilter(e.target.value)}
            >
              <option value="All">All</option>
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
          </div>

          <div className="flex flex-row items-center justify-center col-span-1 xl:col-span-1 mx-auto">
            <button
              onClick={resetAllFilters}
              className="px-2 py-1 bg-red-500 dark:bg-charcoal-800 text-charcoal-800 dark:text-red-500 rounded hover:bg-gray-500 transition text-xs"
            >
              Reset Filters
            </button>
          </div>
        </div>
      </div>
      {userItems?.length === 0 ? (
        <p className="h-screen pt-24 text-2xl">
          No users match selected filters.
        </p>
      ) : (
        <>
          {isMobile ? (
            <div className="grid grid-cols-7 items-center text-center h-12 w-full text-charcoal-800 text-xs font-bold bg-gray-200 dark:bg-gray-900 dark:text-gray-200 dark:border-gray-200 cursor-pointer my-0.25">
              <p className="border-e-charcoal-800 border">ID</p>
              <p className="border-e-charcoal-800 border col-span-2">Email</p>
              <p className="border-e-charcoal-800 border">Role</p>
              <p className="border-e-charcoal-800 border">First Name</p>
              <p className="border-e-charcoal-800 border">Last Name</p>
              <p className="border-e-charcoal-800 border">Action</p>
            </div>
          ) : (
            <div className="grid grid-cols-12 items-center text-center h-12 w-full text-charcoal-800 text-xs font-bold bg-gray-200 dark:bg-gray-900 dark:text-gray-200 dark:border-gray-200 cursor-pointer my-0.25">
              <p className="border-e-charcoal-800 border">ID</p>
              <p className="border-e-charcoal-800 border col-span-2">Email</p>
              <p className="border-e-charcoal-800 border">Role</p>
              <p className="border-e-charcoal-800 border">First Name</p>
              <p className="border-e-charcoal-800 border">Last Name</p>
              <p className="border-e-charcoal-800 border">
                Account. Non Expired
              </p>
              <p className="border-e-charcoal-800 border">
                Account. Non Locked
              </p>
              <p className="border-e-charcoal-800 border">
                Credentials Non Expired
              </p>
              <p className="border-e-charcoal-800 border">Enabled</p>
              <p className="border-e-charcoal-800 border">
                Created At /<br />
                Last Update
              </p>
              <p className="border-e-charcoal-800 border">Action</p>
            </div>
          )}
          <ul className="flex flex-col">
            {isMobile
              ? userItems.map((user) => (
                  <li key={user.id}>
                    <div className="grid grid-cols-7 items-center text-center h-20 w-full text-charcoal-800 text-xs bg-gray-200 dark:bg-gray-900 hover:bg-gray-300 dark:text-gray-200 dark:border-gray-200 dark:hover:bg-gray-800 cursor-pointer my-0.25">
                      <p>{user.id}</p>
                      <p className="col-span-2 truncate max-w-full overflow-hidden text-ellipsis">
                        {user.email}
                      </p>
                      <p>{user.role}</p>
                      <p className="truncate max-w-full overflow-hidden text-ellipsis">
                        {user.firstName}
                      </p>
                      <p className="truncate max-w-full overflow-hidden text-ellipsis">
                        {user.lastName}
                      </p>
                      <div className="flex flex-col justify-around items-center p-1">
                        <button
                          className="bg-green-500 hover:bg-green-800 text-charcoal-800 cursor-pointer p-1 w-12 m-1 rounded-sm"
                          onClick={() => handleEditClick(user.id)}
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
                  </li>
                ))
              : userItems?.map((user) => (
                  <li key={user.id}>
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
                          onClick={() => handleEditClick(user.id)}
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
                  </li>
                ))}
          </ul>

          <ListItemsPagination listItemsPagination={usersPagination} />
        </>
      )}
    </PageContent>
  );
};

export default AdminUsers;
