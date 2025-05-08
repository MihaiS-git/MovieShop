import { useState } from "react";
import MovieList from "../components/movies/MovieList";
import { useGetMoviesQuery } from "../features/movies/movieApi";
import PageContent from "../PageContent";

const MovieListPage = () => {
  const limit = 10;
  const [page, setPage] = useState(1);
  const { data, error, isLoading } = useGetMoviesQuery({ page, limit });
  const { movies, totalCount } = data || { movies: [], totalCount: 0 };

  const totalPages = Math.ceil(totalCount / limit);

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
  
  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading movies</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center mx-auto px-6 py-24 w-full">
      <MovieList movies={movies} totalPages={totalPages} page={page} handleNextPage={handleNextPage} handlePrevPage={handlePrevPage}/>
    </PageContent>
  );
};

export default MovieListPage;
