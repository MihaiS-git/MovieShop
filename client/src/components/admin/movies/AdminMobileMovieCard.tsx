import { MovieItem } from "@/types/Movie";
import { useNavigate } from "react-router-dom";

const AdminMobileMovieCard: React.FC<{
  movie: MovieItem;
  handleDeleteClick: () => void;
}> = ({ movie, handleDeleteClick }) => {
  const navigate = useNavigate();

  const handleEditClick = () => {
    navigate(`${movie.id}`);
  };

  return (
    <div className="grid grid-cols-6 items-center h-15 w-full text-charcoal-800 text-xs bg-gray-200 cursor-pointer my-0.25">
      <img
        src={`/movie_placeholder.png`}
        alt="Movie Poster"
        className="h-15 w-15"
      />
      <p className="text-charcoal-800 col-span-2">{movie.title}</p>
      <p>{movie.rating.replace(/_/g, "-")}</p>
      <p>{movie.releaseYear}</p>
      <div className="flex flex-col justify-around items-center">
        <button
          className="bg-green-500 hover:bg-green-800 text-charcoal-800 cursor-pointer p-1 w-12 m-0.5 rounded-sm"
          onClick={handleEditClick}
        >
          Edit
        </button>
        <button
          className="bg-red-400 hover:bg-red-500 text-charcoal-800 cursor-pointer p-1 w-12 m-0.5 rounded-sm"
          onClick={handleDeleteClick}
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default AdminMobileMovieCard;
