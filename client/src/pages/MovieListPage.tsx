import { useState } from "react";
import { useGetMoviesQuery } from "../features/movies/movieApi";

const MovieListPage = () => {
  const [page, setPage] = useState(1);
  const limit = 10;
  const { data, error, isLoading } = useGetMoviesQuery({page, limit});

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading movies</p>;

  const {movies, totalCount} = data || { movies: [], totalCount: 0 };
  const totalPages = Math.ceil(totalCount / limit);

  const handleNextPage = () => {
    if( page < totalPages) {
      setPage(prev => prev + 1);
    }
  };

  const handlePrevPage = () => {
    if(page > 1){
      setPage(prev => prev - 1);
    }
  };


  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">Movies</h1>
      <ul className="space-y-2">
        {movies?.map((movie) => (
          <li key={movie.id} className="border p-2 rounded">
            <h2 className="text-lg font-semibold">{movie.title}</h2>
            <p>{movie.description}</p>
            <p className="text-sm text-gray-500">Rating: {movie.rating}</p>
          </li>
        ))}
      </ul>

      <div className="flex justify-between mt-4">
        <button
          className="px-4 py-2 bg-gray-200 rounded"
          onClick={handlePrevPage}
          disabled={page === 1}
        >
          Previous
        </button>
        <span>{`Page ${page} of ${totalPages}`}</span>
        <button
          className="px-4 py-2 bg-gray-200 rounded"
          onClick={handleNextPage}
          disabled={page === totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default MovieListPage;
