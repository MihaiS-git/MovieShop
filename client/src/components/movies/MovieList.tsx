import { Movie } from "../../types/Movie";

import MovieCard from "./MovieCard";
import MoviesPagination from "./MoviesPagination";

const MovieList: React.FC<{
  movies: Movie[];
  totalPages: number;
  page: number;
  handleNextPage: () => void;
  handlePrevPage: () => void;
  onPageChange: (pageNo: number) => void;
}> = ({
  movies,
  totalPages,
  page,
  handleNextPage,
  handlePrevPage,
  onPageChange,
}) => {
  return (
    <div className="flex flex-col justify-around align-middle text-center">
      <h1 className="font-bold text-2xl mb-4 text-charcoal-800 dark:text-gold-500">
        Available Movies
      </h1>
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
        {movies?.map((movie) => (
          <li key={movie.id} className="px-0">
            <MovieCard movie={movie} />
          </li>
        ))}
      </ul>

      <MoviesPagination
        totalPages={totalPages}
        page={page}
        handleNextPage={handleNextPage}
        handlePrevPage={handlePrevPage}
        onPageChange={onPageChange}
      />
    </div>
  );
};

export default MovieList;
