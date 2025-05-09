import MovieDetails from "@/components/movie/MovieDetails";
import PageContent from "@/PageContent";
import { useParams } from "react-router-dom";
import { useGetMovieByIdQuery } from "@/features/movies/movieApi";

const MovieDetailsPage = () => {
  const { id } = useParams<{ id: string }>();
  const movieId = Number(id);

  const {data: movie, isLoading, error} = useGetMovieByIdQuery(movieId);

  if(isLoading) return <p>Loading...</p>
  if(error) return <p>Error loading movie details.</p>
  if(!movie) return <p>Movie not found.</p>

  return (
    <PageContent className="flex flex-col items-center justify-center mx-auto px-6 py-24 w-full">
      <MovieDetails movie={movie}/>
    </PageContent>
  );
};

export default MovieDetailsPage;
