import { MovieItem } from "../../types/Movie";
import { useNavigate } from "react-router-dom";

const MovieCard: React.FC<{ movie: MovieItem; handleDeleteClick: () => void }> = ({ movie, handleDeleteClick }) => {
  const navigate = useNavigate();

  const handleEditClick = () => {
    navigate(`${movie.id}`);
  };

  return (
    <div className="flex flex-col justify-between items-center h-80 w-50 mx-auto text-charcoal-800 bg-gray-200 cursor-pointer rounded-md shadow-charcoal-800/50 shadow-[8px_8px_10px_-3px_rgba(0,0,0,0.3)] dark:shadow-gray-400/50">
      <img
        src={`/movie_placeholder.png`}
        alt="Movie Poster"
        className="h-40 w-full rounded-t-md mt-0"
      />
      <h1 className="font-semibold mt-4 mb-2 text-charcoal-800 text-">
        {movie.title}
      </h1>
      <p className="text-xs text-center px-2 line-clamp-3">
        {movie.description}
      </p>
      <p className="text-xs font-bold">{movie.rating.replace(/_/g, "-")}</p>
      <div className="flex gap-1">
        <button
          className="bg-green-500 hover:bg-green-800 text-charcoal-800 text-xs px-2 py-1 mx-auto mb-4 w-20 cursor-pointer rounded-sm"
          onClick={handleEditClick}
        >
          Edit
        </button>
        <button
          className="bg-red-400 hover:bg-red-500 text-charcoal-800 text-xs px-2 py-1 mx-auto mb-4 w-20 cursor-pointer rounded-sm"
          onClick={handleDeleteClick}
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default MovieCard;
