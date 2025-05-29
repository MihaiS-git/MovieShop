import { useIsMobile } from "@/hooks/useIsMobile";
import { MovieItem } from "../../types/Movie";

import MovieCard from "./MovieCard";
import MoviesPagination from "./MoviesPagination";
import { useEffect, useRef } from "react";
import MovieListFiltersBlock from "../movie/MovieListFiltersBlock";

type Props = {
  totalPages: number;
  page: number;
  handleNextPage: () => void;
  handlePrevPage: () => void;
  onPageChange: (pageNo: number) => void;
  hasMore: boolean;
  loadMore: () => void;
  orderBy: string;
  setOrderBy: (order: string) => void;
  ratingFilter: string;
  setRatingFilter: (rating: string) => void;
  yearFilter: number;
  setYearFilter: (year: number) => void;
  categoryFilter: string;
  setCategoryFilter: (category: string) => void;
  searchTerm: string;
  handleSearchTermChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  movies: MovieItem[];
};

const MovieList: React.FC<Props> = ({
  totalPages,
  page,
  handleNextPage,
  handlePrevPage,
  onPageChange,
  hasMore,
  loadMore,
  orderBy,
  setOrderBy,
  ratingFilter,
  setRatingFilter,
  yearFilter,
  setYearFilter,
  categoryFilter,
  setCategoryFilter,
  searchTerm,
  handleSearchTermChange,
  movies,
}) => {
  const isMobile = useIsMobile();
  const loaderRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!isMobile || !hasMore) return;

    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          // Debounced load more
          loadMore();
        }
      },
      { threshold: 1.0 }
    );

    const ref = loaderRef.current;
    if (ref) observer.observe(ref);

    return () => {
      if (ref) observer.unobserve(ref);
    };
  }, [isMobile, hasMore, loadMore]);

  return (
    <div className="flex flex-col justify-around align-middle text-center">
      <h1 className="font-bold text-2xl mb-4 text-charcoal-800 dark:text-gold-500">
        Available Movies
      </h1>

      <MovieListFiltersBlock
        orderBy={orderBy}
        setOrderBy={setOrderBy}
        ratingFilter={ratingFilter}
        setRatingFilter={setRatingFilter}
        yearFilter={yearFilter}
        setYearFilter={setYearFilter}
        categoryFilter={categoryFilter}
        setCategoryFilter={setCategoryFilter}
        searchTerm={searchTerm}
        handleSearchTermChange={handleSearchTermChange}
      />

      {movies.length === 0 ? (
        <p className="h-screen pt-24 text-2xl">
          No movies match selected filters.
        </p>
      ) : (
        <>
          <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 2xl:grid-cols-4 gap-4">
            {movies?.map((movie: MovieItem) => (
              <li key={movie.id} className="px-0">
                <MovieCard movie={movie} />
              </li>
            ))}
          </ul>

          {isMobile ? (
            hasMore ? (
              <div
                ref={loaderRef}
                className="mt-6 h-16 text-sm text-charcoal-800 flex items-center justify-center"
              >
                <div role="status">
                  <svg
                    aria-hidden="true"
                    className="w-8 h-8 text-gray-200 animate-spin dark:text-gray-600 fill-charcoal-800 dark:fill-red-600"
                    viewBox="0 0 100 101"
                    fill="none"
                  >
                    <path
                      d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908Z"
                      fill="currentColor"
                    />
                    <path
                      d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                      fill="currentFill"
                    />
                  </svg>
                </div>
              </div>
            ) : (
              <div className="mt-6 text-charcoal-800 text-sm">
                No more movies.
              </div>
            )
          ) : (
            <MoviesPagination
              totalPages={totalPages}
              page={page}
              handleNextPage={handleNextPage}
              handlePrevPage={handlePrevPage}
              onPageChange={onPageChange}
            />
          )}
        </>
      )}
    </div>
  );
};

export default MovieList;
