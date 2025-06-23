import { ROLE } from "@/types/User";
import { UserFilters } from "@/types/UserFilters";

type Props = {
  userFilters: UserFilters;
};

const UserListFiltersBlock = ({ 
  userFilters 
}: Props) => {
  const {
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
  } = userFilters;

  return (
    <>
      <div className="w-full mb-4 rounded-lg bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-sm">
        <div className="w-full grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-6 xl:grid-cols-8 2xl:grid-cols-10 p-2 gap-3 sm:gap-x-5 md:gap-x-10 md:px-12 lg:gap-x-2 lg:px-2">
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
    </>
  );
};

export default UserListFiltersBlock;
