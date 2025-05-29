import { useDeleteMovieByIdMutation } from "@/features/movies/movieApi";

import PageContent from "@/PageContent";
import AdminMovieList from "@/components/movies/AdminMovieList";
import usePaginatedMovies from "@/hooks/usePaginatedMovies";

const EditMovies = () => {
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
  } = usePaginatedMovies();

  const [deleteMovie] = useDeleteMovieByIdMutation();

  const handleDeleteClick = async (movieId: number) => {
    try {
      await deleteMovie(movieId);
      setMovies((prev) => prev.filter((m) => m.id !== movieId));
    } catch (err) {
      console.error("Failed to delete movie: ", err);
    }
  };

  if (isLoading && page === 1) return <p>Loading...</p>;
  if (error) return <p>Error loading movies.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center pt-4 pb-24 w-full">
      <AdminMovieList
        movies={movies}
        totalPages={totalPages}
        page={page}
        handleNextPage={handleNextPage}
        handlePrevPage={handlePrevPage}
        onPageChange={onPageChange}
        hasMore={hasMore}
        loadMore={loadMore}
        handleDeleteClick={handleDeleteClick}
        orderBy={orderBy}
        setOrderBy={setOrderBy}
        ratingFilter={ratingFilter}
        setRatingFilter={setRatingFilter}
        yearFilter={yearFilter}
        setYearFilter={setYearFilter}
        categoryFilter={categoryFilter}
        setCategoryFilter={setCategoryFilter}
        searchTerm={searchTerm}
        handleSearchTermChange={handleSearchTermChange}
      />
    </PageContent>
  );
};

export default EditMovies;
