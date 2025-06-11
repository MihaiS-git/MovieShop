export type ItemsPagination = {
  totalPages: number;
  page: number;
  handleNextPage: () => void;
  handlePrevPage: () => void;
  onPageChange: (pageNo: number) => void;
  hasMore: boolean;
  loadMore: () => void;
};
