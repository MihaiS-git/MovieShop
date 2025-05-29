import { useCallback, useEffect, useMemo, useState } from "react";
import MovieList from "../../components/movies/MovieList";
import { useGetMoviesQuery } from "../../features/movies/movieApi";
import PageContent from "../../PageContent";
import { useIsMobile } from "@/hooks/useIsMobile";
import { MovieItem } from "@/types/Movie";

const MovieListPage = () => {
  const limit = 16;
  const isMobile = useIsMobile();

  const [page, setPage] = useState(1);
  const [movies, setMovies] = useState<MovieItem[]>([]);
  const [orderBy, setOrderBy] = useState("None");
  const [ratingFilter, setRatingFilter] = useState("All");
  const [yearFilter, setYearFilter] = useState(0);
  const [categoryFilter, setCategoryFilter] = useState("All");

  const [resetFilters, setResetFilters] = useState(false);

  const sortField = useMemo(() => getSortField(orderBy), [orderBy]);

  const { data, error, isLoading, isFetching } = useGetMoviesQuery({
    page: page - 1,
    limit,
    orderBy: sortField,
    ratingFilter,
    yearFilter,
    categoryFilter,
  });

  const totalCount = data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / limit);
  const hasMore = page < totalPages;

  useEffect(() => {
    setResetFilters(true);
    setPage(1);
  }, [orderBy, ratingFilter, yearFilter, categoryFilter]);

  useEffect(() => {
    if (!data) return;

    const isFirstPage = page === 1;

    if (resetFilters || isFirstPage) {
      setMovies(data.movies);
      setResetFilters(false);
    } else {
      setMovies((prev) => {
        const prevIds = new Set(prev.map((m) => m.id));
        const newMovies = data.movies.filter((m) => !prevIds.has(m.id));
        if (newMovies.length === 0) return prev;
        return isMobile ? [...prev, ...newMovies] : [...newMovies];
      });
    }
  }, [data, isMobile, page, resetFilters]);

  const handleNextPage = () => {
    if (page < totalPages) {
      setPage((prev) => prev + 1);
    }
  };

  const handlePrevPage = () => {
    if (page > 1) {
      setPage((prev) => prev - 1);
    }
  };

  const onPageChange = (pageNo: number) => {
    setPage(pageNo);
  };

  const loadMore = useCallback(() => {
    if (!isFetching && hasMore && !isLoading && !resetFilters) {
      setPage((prev) => {
        // Avoid setting the same page again
        const nextPage = prev + 1;
        return nextPage <= totalPages ? nextPage : prev;
      });
    }
  }, [isFetching, hasMore, isLoading, totalPages, resetFilters]);

  if (isLoading && page == 1) return <p>Loading...</p>;
  if (error) return <p>Error loading movies</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center mx-auto px-6 py-24 w-full h-min-screen">
      <MovieList
        totalPages={totalPages}
        page={page}
        handleNextPage={handleNextPage}
        handlePrevPage={handlePrevPage}
        onPageChange={onPageChange}
        hasMore={hasMore}
        loadMore={loadMore}
        orderBy={orderBy}
        setOrderBy={setOrderBy}
        ratingFilter={ratingFilter}
        setRatingFilter={setRatingFilter}
        yearFilter={yearFilter}
        setYearFilter={setYearFilter}
        categoryFilter={categoryFilter}
        setCategoryFilter={setCategoryFilter}
        movies={movies}
      />
    </PageContent>
  );
};

function getSortField(order: string): string | undefined {
  switch (order) {
    case "Title Ascending":
      return "title_asc";
    case "Title Descending":
      return "title_desc";
    case "Rating Ascending":
      return "rating_asc";
    case "Rating Descending":
      return "rating_desc";
    default:
      return undefined;
  }
}

export default MovieListPage;
