import MovieList from "../../components/movies/MovieList";
import PageContent from "../../PageContent";
import usePaginatedMovies from "@/hooks/usePaginatedMovies";

const MovieListPage = () => {
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
    error} = usePaginatedMovies();

  if (isLoading && page == 1) return <p>Loading...</p>;
  if (error) return <p>Error loading movies</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center mx-auto px-6 py-24 w-full h-min-screen">
      <MovieList
        totalPages={totalPages}
        page={page}
        handleNextPage={handleNextPage}
        handlePrevPage={handlePrevPage}
        onPageChange={onPageChange}
        hasMore={hasMore}
        loadMore={loadMore}
        orderBy={orderBy}
        setOrderBy={setOrderBy}
        ratingFilter={ratingFilter}
        setRatingFilter={setRatingFilter}
        yearFilter={yearFilter}
        setYearFilter={setYearFilter}
        categoryFilter={categoryFilter}
        setCategoryFilter={setCategoryFilter}
        movies={movies}
      />
    </PageContent>
  );
};



export default MovieListPage;
