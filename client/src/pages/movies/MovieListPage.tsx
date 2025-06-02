import { useSearchParams } from "react-router-dom";
import MovieList from "../../components/movies/MovieList";
import PageContent from "../../PageContent";
import { useGetMoviesQuery } from "@/features/movies/movieApi";
import { useEffect, useRef, useState } from "react";
import { MovieItem } from "@/types/Movie";
import { useIsMobile } from "@/hooks/useIsMobile";

const MovieListPage = () => {
  const isMobile = useIsMobile();
  const [searchParams, setSearchParams] = useSearchParams();

  const rawPage = parseInt(searchParams.get("page") || "1", 10);
  const apiPage = rawPage - 1;
  const limit = parseInt(searchParams.get("limit") || "16", 10);
  const orderBy = searchParams.get("orderBy") || "None";
  const ratingFilter = searchParams.get("rating") || "All";
  const yearFilter = parseInt(searchParams.get("year") || "0", 10);
  const categoryFilter = searchParams.get("category") || "All";
  const searchTerm = searchParams.get("searchTerm") || "";

  const [movies, setMovies] = useState<MovieItem[]>([]);
  const prevFilters = useRef({
    orderBy,
    ratingFilter,
    yearFilter,
    categoryFilter,
    searchTerm,
  });

  const updateFilters = (newFilters: Record<string, string | number>) => {
    const params = new URLSearchParams(searchParams);
    Object.entries(newFilters).forEach(([key, value]) => {
      if (value === "All" || value === "None" || value === "" || value === 0) {
        params.delete(key);
      } else {
        params.set(key, String(value));
      }
    });
    params.set("page", "1");
    setSearchParams(params);
  };

  const goToPage = (newPage: number) => {
    const params = new URLSearchParams(searchParams);
    params.set("page", newPage.toString());
    setSearchParams(params);
  };

  const { data, isLoading, error } = useGetMoviesQuery({
    page: apiPage,
    limit,
    orderBy: orderBy === "None" ? undefined : orderBy,
    ratingFilter,
    yearFilter,
    categoryFilter,
    titleFilter: searchTerm,
  });

  useEffect(() => {
    const filtersChanged =
      prevFilters.current.orderBy !== orderBy ||
      prevFilters.current.ratingFilter !== ratingFilter ||
      prevFilters.current.yearFilter !== yearFilter ||
      prevFilters.current.categoryFilter !== categoryFilter ||
      prevFilters.current.searchTerm !== searchTerm;

    if (filtersChanged || rawPage === 1 || searchParams.toString() === "" || !isMobile) {
      setMovies(data?.movies || []);
    } else if (data?.movies) {
      setMovies((prev: MovieItem[]) => {
        const prevIds = new Set(prev.map((m) => m.id));
        const newMovies = data.movies.filter((m) => !prevIds.has(m.id));
        return [...prev, ...newMovies];
      });
    }

    prevFilters.current = {
      orderBy,
      ratingFilter,
      yearFilter,
      categoryFilter,
      searchTerm,
    };
  }, [
    data,
    orderBy,
    ratingFilter,
    yearFilter,
    categoryFilter,
    searchTerm,
    rawPage,
    searchParams,
    isMobile,
  ]);

  const totalCount = data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / limit);
  const hasMore = rawPage < totalPages;

  const setOrderBy = (value: string) => updateFilters({ orderBy: value });
  const setRatingFilter = (value: string) => updateFilters({ rating: value });
  const setYearFilter = (value: number) => updateFilters({ year: value });
  const setCategoryFilter = (value: string) =>
    updateFilters({ category: value });
  const handleSearchTermChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    updateFilters({ searchTerm: e.target.value });
  };

  const handleNextPage = () => goToPage(rawPage + 1);
  const handlePrevPage = () => goToPage(rawPage > 1 ? rawPage - 1 : 1);
  const onPageChange = (newPage: number) => goToPage(newPage);
  const resetAllFilters = () => {
    const params = new URLSearchParams();
    params.set("page", "1");
    setSearchParams(params);
  };

  if (isLoading && rawPage === 1) return <p>Loading...</p>;
  if (error) return <p>Error loading movies</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center mx-auto px-6 py-24 w-full h-min-screen">
      <MovieList
        moviePagination={{
          totalPages,
          page: rawPage,
          handleNextPage,
          handlePrevPage,
          onPageChange,
          hasMore,
          loadMore: handleNextPage,
        }}
        movieFilters={{
          orderBy,
          setOrderBy,
          ratingFilter,
          setRatingFilter,
          yearFilter,
          setYearFilter,
          categoryFilter,
          setCategoryFilter,
          searchTerm,
          handleSearchTermChange,
          resetAllFilters,
        }}
        movies={movies || []}
      />
    </PageContent>
  );
};

export default MovieListPage;
