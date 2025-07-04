import { useDeleteMovieByIdMutation } from "@/features/movies/movieApi";

import PageContent from "@/PageContent";
import {usePaginatedMovies} from "@/hooks/usePaginatedMovies";
import { useEffect } from "react";
import AdminMovieList from "@/components/admin/movies/AdminMovieList";

const AdminMoviesListPage = () => {
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
    ratingFilter,
    setRatingFilter,
    yearFilter,
    setYearFilter,
    categoryFilter,
    setCategoryFilter,
    movies,
    isLoading,
    error,
    searchTerm,
    handleSearchTermChange,
    setMovies,
    resetAllFilters,
  } = usePaginatedMovies();

  const [deleteMovie] = useDeleteMovieByIdMutation();

  const handleDelete = async (movieId: number) => {
    try {
      await deleteMovie(movieId);
      setMovies((prev) => prev.filter((m) => m.id !== movieId));
    } catch (err: unknown) {
      console.error("Failed to delete movie: ", err);
    }
  };

  if (isLoading && page === 1) return <p>Loading...</p>;
  if (error) return <p>Error loading movies.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center pt-4 pb-24 w-full">
      <AdminMovieList
        moviePagination={{
          totalPages,
          page,
          handleNextPage,
          handlePrevPage,
          onPageChange,
          hasMore,
          loadMore,
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
        movies={movies}
        handleDelete={handleDelete}
      />
    </PageContent>
  );
};

export default AdminMoviesListPage;
