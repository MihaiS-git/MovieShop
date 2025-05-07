import { useGetMoviesQuery } from "../features/movies/movieApi";

const MovieListPage = () => {
  const { data: movies, error, isLoading } = useGetMoviesQuery();

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading movies</p>;

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
    </div>
  );
};

export default MovieListPage;
