import { MovieItem } from "../../types/Movie";
import { RootState } from "../../app/store";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { formatCurrency } from "@/util/formatCurrency";

const MovieCard: React.FC<{ movie: MovieItem }> = ({ movie }) => {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);
  const navigate = useNavigate();

  const handleCardClick = () => {
    navigate(`/movies/${movie.id}`);
  };

  return (
    <div
      className="flex flex-col justify-between items-center h-90 w-60 mx-auto bg-charcoal-800 dark:bg-red-500 cursor-pointer rounded-md shadow-charcoal-800 shadow-[5px_5px_8px_-2px_rgba(0,0,0,0.8)] dark:shadow-gray-600 pb-4"
      onClick={handleCardClick}
    >
      <img
        src={`/movie_placeholder.png`}
        alt={movie.title}
        className="h-40 w-full rounded-t-md mt-0"
      />
      <h1 className="font-semibold mt-4 mb-2 text-gold-500 text-">
        {movie.title}
      </h1>
      <p className="text-red-500 dark:text-charcoal-800 text-xs text-center px-2 pb-2 line-clamp-4">
        {movie.description}
      </p>
      <div className="flex flex-row w-full justify-center">
        <p className="text-gold-500 dark:text-charcoal-800 text-xs px-2">
          {movie.releaseYear}
        </p>

        <p className="text-gold-500 dark:text-charcoal-800 text-xs px-1">
          {movie.length}min
        </p>

        <p className="text-gold-500 dark:text-charcoal-800 text-xs px-1">
          {movie.rating.replace(/_/g, "-")}
        </p>
        <p className="text-gold-500 dark:text-charcoal-800 text-xs px-2">
          {movie.language}
        </p>
      </div>
      <p className="text-gold-500 dark:text-charcoal-800 text- font-bold px-2 pb-2">
        {formatCurrency(Number(movie.rentalRate))}
      </p>

      {isAuthenticated && (
        <button className="bg-gold-300 hover:bg-gold-500 text-charcoal-800 text-xs px-2 py-1 w-2/5 mx-auto mb-4 cursor-pointer rounded-sm">
          Add To Cart
        </button>
      )}
    </div>
  );
};

export default MovieCard;
