import MovieSearchResultRow from "@/components/admin/MovieSearchResultRow";
import { useLazySearchMoviesByTitleQuery } from "@/features/movies/movieApi";
import PageContent from "@/PageContent";
import { MovieItem } from "@/types/Movie";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const SearchMoviePage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [searchResult, setSearchResult] = useState<MovieItem[]>([]);
  const [searchMovies] = useLazySearchMoviesByTitleQuery();
  const navigate = useNavigate();

  const handleSearchTermChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchTerm.trim()) return;

    try {
      const result = await searchMovies(searchTerm).unwrap();
      setSearchResult(result);
    } catch (err: unknown) {
      console.error("Search failed ", err);
      setSearchResult([]);
    }
  };

  const handleMovieClick = (movieId: number) => {
    navigate(`/admin/movies/edit/${movieId}`);
  };

  return (
    <PageContent className="flex flex-col items-center justify-center pt-4 pb-24 w-full">
      <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 p-2 mb-4 rounded-2xl">
        Search Movie
      </h1>
      <form
        className="bg-gray-200 text-sm flex flex-col gap-1  w-10/12 lg:w-full max-w-md border border-charcoal-800 py-2 px-4"
        onSubmit={handleFormSubmit}
      >
        <label htmlFor="searchMovie">
          <input
            type="text"
            id="searchMovie"
            name="searchMovie"
            value={searchTerm}
            onChange={handleSearchTermChange}
            className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
            placeholder="Enter movie title..."
          />
          <button
            type="submit"
            className="bg-green-600 hover:bg-green-500 rounded-sm mt-2 p-1 w-full"
          >
            Search
          </button>
        </label>
      </form>
      <div className="bg-gray-200 text-xs sm:text-sm flex flex-col mt-4 gap-1 w-11/12 sm:w-10/12 min-h-80 lg:w-full max-w-xl border border-charcoal-800 py-2 px-2 sm:px-4">
        {searchResult && searchResult.length > 0 ? (
          <div>
            <MovieSearchResultRow
              id="#ID"
              title="Title"
              releaseYear="Release Year"
              length="Length"
              rating="Rating"
              isHeader={true}
            />
            <ul>
              {searchResult.map((movie) => (
                <li
                  key={movie.id}
                  className="cursor-pointer border bg-gray-400 hover:bg-gray-300 transition text-gray-900"
                  onClick={() => handleMovieClick(movie.id)}
                >
                  <MovieSearchResultRow
                    id={movie.id}
                    title={movie.title}
                    releaseYear={movie.releaseYear}
                    length={movie.length}
                    rating={movie.rating}
                    isHeader={false}
                  />
                </li>
              ))}
            </ul>
          </div>
        ) : (
          <div className="text-gray-500 text-sm text-center w-full h-full">
            <p className="py-30 mx-auto">No results found.</p>
          </div>
        )}
      </div>
    </PageContent>
  );
};

export default SearchMoviePage;
