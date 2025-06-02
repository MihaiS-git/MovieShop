import { useEffect, useState } from "react";
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

  const { data, error, isLoading, isFetching } = useGetMoviesQuery({
    page: page - 1,
    limit,
    orderBy: orderBy === "None" ? undefined : orderBy,
    ratingFilter: ratingFilter === "All" ? undefined : ratingFilter,
    yearFilter: yearFilter === 0 ? undefined : yearFilter,
    categoryFilter: categoryFilter === "All" ? undefined : categoryFilter,
    titleFilter:
      debounceSearchTerm.trim() === "" ? undefined : debounceSearchTerm.trim(),
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
    setPage(1);
  }, [orderBy, ratingFilter, yearFilter, categoryFilter, debounceSearchTerm]);

  useEffect(() => {
    if (!data || !data.movies) return;

    setMovies((prev) => {
      if (page === 1) return data.movies;

      const prevIds = new Set(prev.map((m) => m.id));
      const newMovies = data.movies.filter((m) => !prevIds.has(m.id));

      if (newMovies.length === 0) return prev;

      return isMobile ? [...prev, ...newMovies] : [...newMovies];
    });
  }, [data, isMobile, page]);

  const resetAllFilters = () => {
    setOrderBy("");
    setRatingFilter("All");
    setYearFilter(0);
    setCategoryFilter("All");
    setSearchTerm("");
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
    // Prevent triggering while first page is still loading
    if (page === 1 && (isLoading || isFetching)) return;

    if (!isLoading && !isFetching && hasMore) {
      setPage((prev) => prev + 1);
    }
  };

  const handleSearchTermChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
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
    resetAllFilters,
  };
};

export default usePaginatedMovies;
