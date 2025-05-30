export type MovieFilters = {
  orderBy: string;
  setOrderBy: (value: string) => void;
  ratingFilter: string;
  setRatingFilter: (value: string) => void;
  yearFilter: number;
  setYearFilter: (value: number) => void;
  categoryFilter: string;
  setCategoryFilter: (category: string) => void;
  searchTerm: string;
  handleSearchTermChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  resetAllFilters: () => void;
};

export type MoviePagination = {
  totalPages: number;
  page: number;
  handleNextPage: () => void;
  handlePrevPage: () => void;
  onPageChange: (pageNo: number) => void;
  hasMore: boolean;
  loadMore: () => void;
};
