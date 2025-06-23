import { useIsMobile } from "./useIsMobile";
import { useEffect, useState } from "react";
import { UserItem } from "@/types/User";
import { useGetUsersQuery } from "@/features/users/userApi";

export function usePaginatedUsers() {
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

  const { data, error, isLoading, isFetching } = useGetUsersQuery(
    {
      page: page - 1,
      limit,
      orderBy: orderBy == "None" ? undefined : orderBy,
      roleFilter: roleFilter === "All" ? undefined : roleFilter,
      searchFilter:
        debounceSearchTerm.trim() === ""
          ? undefined
          : debounceSearchTerm.trim(),
      enabledFilter: enabledFilter === "All" ? undefined : enabledFilter,
      accountNonExpiredFilter:
        accountNonExpiredFilter === "All" ? undefined : accountNonExpiredFilter,
      accountNonLockedFilter:
        accountNonLockedFilter === "All" ? undefined : accountNonLockedFilter,
      credentialsNonExpiredFilter:
        credentialsNonExpiredFilter === "All"
          ? undefined
          : credentialsNonExpiredFilter,
    }
  );

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
  }, [orderBy, roleFilter, debounceSearchTerm, enabledFilter, accountNonExpiredFilter, accountNonLockedFilter, credentialsNonExpiredFilter]);

  useEffect(() => {
    if (!data || !data.users) return;

    setUserItems((prev) => {
        if (page === 1) return data.users;

        const prevIds = new Set(prev.map((u) => u.id));
        const newUsers = data.users.filter((u) => !prevIds.has(u.id));
        if(newUsers.length === 0) return prev;
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
    setPage(1);
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

  return {
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
    debounceSearchTerm,
    setDebounceSearchTerm,
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
    error,
    isLoading,
    isFetching,
    userItems,
    setUserItems,
    resetAllFilters,
  };
}
