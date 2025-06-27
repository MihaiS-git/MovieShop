import { useIsMobile } from "@/hooks/useIsMobile";

import AdminMovieCard from "./AdminMovieCard";
import AdminMobileMovieCard from "./AdminMobileMovieCard";
import { MovieFilters } from "@/types/MovieFilters";
import useInfiniteScroll from "@/hooks/useInfiniteScroll";
import { ItemsPagination } from "@/types/ItemsPagination";
import { MovieItem } from "@/types/Movie";
import MovieListFiltersBlock from "@/components/movie/MovieListFiltersBlock";
import ListItemsPagination from "@/components/ui/ListItemsPagination";

type Props = {
  moviePagination: ItemsPagination;
  movieFilters: MovieFilters;
  movies: MovieItem[];
  handleDelete: (id: number) => void;
};

const AdminMovieList: React.FC<Props> = ({
  moviePagination,
  movieFilters,
  movies,
  handleDelete,
}) => {
  const isMobile = useIsMobile();
  const { hasMore, loadMore, totalPages } = moviePagination;

  const loaderRef = useInfiniteScroll(loadMore, hasMore);

  return (
    <div className="flex flex-col justify-around align-middle text-center px-4">
      <div className="w-full h-full text-red-500 dark:text-charcoal-800 flex flex-col pb-4 items-center">
        <h1 className="bg-charcoal-800 dark:bg-red-500 text-base lg:text-lg text-center w-50 p-2 rounded-2xl">
          Movies List
        </h1>
      </div>
      <MovieListFiltersBlock movieFilters={movieFilters} />
      {movies.length === 0 ? (
        <p className="h-screen pt-24 text-2xl">
          No movies match selected filters.
        </p>
      ) : (
        <>
          {isMobile ? (
            <div className="grid grid-cols-6 items-center h-12 w-full text-charcoal-800 text-xs font-bold bg-gray-200 dark:bg-gray-900 dark:text-gray-200 dark:border-gray-200 cursor-pointer my-0.25">
              <p className="border-e-charcoal-800 border">Picture</p>
              <p className="border-e-charcoal-800 border col-span-2">Title</p>
              <p>Rating</p>
              <p className="border-x-charcoal-800 border">Release Year</p>
              <p>Actions</p>
            </div>
          ) : (
            <div className="grid grid-cols-12 items-center h-12 w-full text-charcoal-800 text-xs font-bold bg-gray-200 dark:bg-gray-900 dark:text-gray-200 dark:border-gray-200 cursor-pointer my-0.25">
              <p>Picture</p>
              <p>Title</p>
              <p className="col-span-2 ">Description</p>
              <p>Rating</p>
              <p>Release Year</p>
              <p>Language</p>
              <p>Original Language</p>
              <p>Rental Rate</p>
              <p>Length</p>
              <p>Categories</p>
              <p>Actions</p>
            </div>
          )}

          <ul className="flex flex-col">
            {movies?.map((movie) => (
              <li key={movie.id} className="px-0">
                {isMobile ? (
                  <AdminMobileMovieCard
                    movie={movie}
                    handleDeleteClick={() => handleDelete(movie.id)}
                  />
                ) : (
                  <AdminMovieCard
                    movie={movie}
                    handleDeleteClick={() => handleDelete(movie.id)}
                  />
                )}
              </li>
            ))}
          </ul>

          {isMobile ? (
            hasMore ? (
              <div
                ref={loaderRef}
                className="mt-6 h-16 text-sm text-charcoal-800 flex items-center justify-center"
              >
                {/* Loading spinner */}
                <div role="status">
                  <svg
                    aria-hidden="true"
                    className="w-8 h-8 text-gray-200 animate-spin dark:text-gray-600 fill-charcoal-800 dark:fill-red-600"
                    viewBox="0 0 100 101"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                      fill="currentColor"
                    />
                    <path
                      d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                      fill="currentFill"
                    />
                  </svg>
                  <span className="sr-only">Loading...</span>
                </div>
              </div>
            ) : (
              <div className="mt-6 text-charcoal-800 text-sm">
                No more movies.
              </div>
            )
          ) : (
            totalPages > 1 && (
              <ListItemsPagination listItemsPagination={moviePagination} />
            )
          )}
        </>
      )}
    </div>
  );
};

export default AdminMovieList;
