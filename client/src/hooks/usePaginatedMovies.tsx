import { useCallback, useEffect, useMemo, useState } from "react";
import { useIsMobile } from "./useIsMobile";
import { useGetMoviesQuery } from "@/features/movies/movieApi";
import { MovieItem } from "@/types/Movie";

const usePaginatedMovies = () => {
  const limit = 16;
  const isMobile = useIsMobile();

  const [page, setPage] = useState(1);
  const [movies, setMovies] = useState<MovieItem[]>([]);
  const [orderBy, setOrderBy] = useState("None");
  const [ratingFilter, setRatingFilter] = useState("All");
  const [yearFilter, setYearFilter] = useState(0);
  const [categoryFilter, setCategoryFilter] = useState("All");
  const [searchTerm, setSearchTerm] = useState("");
  const [debounceSearchTerm, setDebounceSearchTerm] = useState(searchTerm);

  const [resetFilters, setResetFilters] = useState(false);

  const sortField = useMemo(() => getSortField(orderBy), [orderBy]);

  const { data, error, isLoading, isFetching } = useGetMoviesQuery({
    page: page - 1,
    limit,
    orderBy: sortField === "None" ? undefined : sortField,
    ratingFilter: ratingFilter === "All" ? undefined : ratingFilter,
    yearFilter: yearFilter === 0 ? undefined : yearFilter,
    categoryFilter: categoryFilter === "All" ? undefined : categoryFilter,
    titleFilter: debounceSearchTerm.trim() === "" ? undefined : debounceSearchTerm.trim(),
  });

  const totalCount = data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / limit);
  const hasMore = page < totalPages;

  useEffect(() => {
    const timeout = setTimeout(() => {
      setDebounceSearchTerm(searchTerm);
    }, 300);

    return () => clearTimeout(timeout);
  }, [searchTerm]);

  useEffect(() => {
    setResetFilters(true);
    setPage(1);
  }, [orderBy, ratingFilter, yearFilter, categoryFilter]);

  useEffect(() => {
    if (!data) return;

    const isFirstPage = page === 1;

    if (resetFilters || isFirstPage) {
      setMovies(data.movies);
      setResetFilters(false);
    } else {
      setMovies((prev) => {
        const prevIds = new Set(prev.map((m) => m.id));
        const newMovies = data.movies.filter((m) => !prevIds.has(m.id));
        if (newMovies.length === 0) return prev;
        return isMobile ? [...prev, ...newMovies] : [...newMovies];
      });
    }
  }, [data, isMobile, page, resetFilters]);

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

  const loadMore = useCallback(() => {
    if (!isFetching && hasMore && !isLoading && !resetFilters) {
      setPage((prev) => {
        // Avoid setting the same page again
        const nextPage = prev + 1;
        return nextPage <= totalPages ? nextPage : prev;
      });
    }
  }, [isFetching, hasMore, isLoading, totalPages, resetFilters]);

  const handleSearchTermChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
    setPage(1);
    setResetFilters(true);
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
    ratingFilter,
    setRatingFilter,
    yearFilter,
    setYearFilter,
    categoryFilter,
    setCategoryFilter,
    movies,
    isLoading,
    isFetching,
    error,
    setMovies,
    searchTerm,
    handleSearchTermChange,
  };
};

function getSortField(order: string): string | undefined {
  switch (order) {
    case "Title Ascending":
      return "title_asc";
    case "Title Descending":
      return "title_desc";
    case "Rating Ascending":
      return "rating_asc";
    case "Rating Descending":
      return "rating_desc";
    default:
      return undefined;
  }
}

export default usePaginatedMovies;
