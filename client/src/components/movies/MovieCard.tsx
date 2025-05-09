import { Movie } from "../../types/Movie";
import { RootState } from "../../app/store";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

const MovieCard: React.FC<{ movie: Movie }> = ({ movie }) => {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);
  const navigate = useNavigate();

  const handleCardClick = () => {
    navigate(`/movies/${movie.id}`);
  };

  return (
    <div
      className="flex flex-col justify-between items-center h-90 w-60 mx-auto bg-charcoal-800 dark:bg-red-500 cursor-pointer rounded-md"
      onClick={handleCardClick}
    >
      <img
        src={`/movie_placeholder.png`}
        alt="Movie Poster"
        className="h-40 w-full rounded-t-md mt-0"
      />
      <h1 className="font-semibold mt-4 mb-2 text-gold-500 text-">
        {movie.title}
      </h1>
      <p className="text-red-500 dark:text-charcoal-800 text-xs text-center px-2 line-clamp-3">
        {movie.description}
      </p>
      <p className="text-red-500 dark:text-charcoal-800 text-xs font-bold">
        {movie.rating}
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
