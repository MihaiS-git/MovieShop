import { formatCurrency } from "@/util/formatCurrency";
import { MovieItem } from "../../types/Movie";
import { useNavigate } from "react-router-dom";

const AdminMovieCard: React.FC<{
  movie: MovieItem;
  handleDeleteClick: () => void;
}> = ({ movie, handleDeleteClick }) => {
  const navigate = useNavigate();

  const handleEditClick = () => {
    navigate(`${movie.id}`);
  };

  return (
    <>

      <div className="grid grid-cols-12 items-center h-20 w-full text-charcoal-800 text-xs bg-gray-200 dark:bg-gray-900 hover:bg-gray-300 dark:text-gray-200 dark:border-gray-200 dark:hover:bg-gray-800 cursor-pointer my-0.25">
        <img
          src={`/movie_placeholder.png`}
          alt="Movie Poster"
          className="h-20 w-20"
        />
        <p>{movie.title}</p>
        <p className="text-start px-2 line-clamp-3 col-span-2">
          {movie.description}
        </p>
        <p>{movie.rating.replace(/_/g, "-")}</p>
        <p>{movie.releaseYear}</p>
        <p>{movie.language}</p>
        <p>{movie.originalLanguage || "N/A"}</p>
        <p>{formatCurrency(movie.rentalRate)}</p>
        <p>{movie.length}</p>
        <ul>
          {movie.categories.map((c) => (
            <li key={c}>{c}</li>
          ))}
        </ul>

        <div className="flex flex-col justify-around items-center">
          <button
            className="bg-green-500 hover:bg-green-800 text-charcoal-800 cursor-pointer p-1 w-15 m-1 rounded-sm"
            onClick={handleEditClick}
          >
            Edit
          </button>
          <button
            className="bg-red-400 hover:bg-red-500 text-charcoal-800 cursor-pointer p-1 w-15 m-1 rounded-sm"
            onClick={handleDeleteClick}
          >
            Delete
          </button>
        </div>
      </div>
    </>
  );
};

export default AdminMovieCard;
