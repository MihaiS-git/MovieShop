import { Movie } from "../../types/Movie";
import MovieCard from "./MovieCard";

const MovieList: React.FC<{
  movies: Movie[];
  totalPages: number;
  page: number;
  handleNextPage: () => void;
  handlePrevPage: () => void;
}> = ({ movies, totalPages, page, handleNextPage, handlePrevPage }) => {

  return (
    <div className="flex flex-col justify-around align-middle text-center">
        <h1 className="font-bold text-2xl mb-4 text-charcoal-800 dark:text-gold-500">Available Movies</h1>
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
        {movies?.map((movie) => (
          <li key={movie.id} className="px-0">
            <MovieCard movie={movie}/>
          </li>
        ))}
      </ul>
      <div className="flex justify-between mt-4">
        <button
          className="px-4 py-2 bg-gold-300 hover:bg-gold-500 rounded"
          onClick={handlePrevPage}
          disabled={page === 1}
        >
          Previous
        </button>
        <span className="text-gold-300 hover:text-gold-500">{`Page ${page} of ${totalPages}`}</span>
        <button
          className="px-4 py-2 bg-gold-300 hover:bg-gold-500 rounded"
          onClick={handleNextPage}
          disabled={page === totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default MovieList;
