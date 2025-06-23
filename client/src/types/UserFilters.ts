export type UserFilters = {
    orderBy: string,
    setOrderBy: (value: string) => void,
    roleFilter: string,
    setRoleFilter: (value: string) => void,
    enabledFilter: string,
    setEnabledFilter: (value: string) => void,
    accountNonExpiredFilter: string,
    setAccountNonExpiredFilter: (value: string) => void,
    accountNonLockedFilter: string,
    setAccountNonLockedFilter: (value: string) => void,
    credentialsNonExpiredFilter: string,
    setCredentialsNonExpiredFilter: (value: string) => void,
    searchFilter: string,
    handleSearchTermChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    resetAllFilters: () => void;
};