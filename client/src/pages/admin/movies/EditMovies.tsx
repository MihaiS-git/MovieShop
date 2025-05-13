import {
  useDeleteMovieByIdMutation,
  useGetMoviesQuery,
} from "@/features/movies/movieApi";
import { useIsMobile } from "@/hooks/useIsMobile";
import { Movie } from "@/types/Movie";
import { useCallback, useEffect, useState } from "react";

import PageContent from "@/PageContent";
import AdminMovieList from "@/components/movies/AdminMovieList";

const EditMovies = () => {
  const limit = 16;
  const isMobile = useIsMobile();
  const [deleteMovie] = useDeleteMovieByIdMutation();

  const [page, setPage] = useState(1);
  const [movies, setMovies] = useState<Movie[]>([]);

  const { data, error, isLoading, isFetching } = useGetMoviesQuery({
    page: page - 1,
    limit,
  });
  const totalCount = data?.totalCount || 0;

  const totalPages = Math.ceil(totalCount / limit);
  const hasMore = page < totalPages;

  useEffect(() => {
    if (!data) return;

    if (isMobile) {
      setMovies((prev) => {
        const existingIds = new Set(prev.map((m) => m.id));
        const newUniqueMovies = (data.movies || []).filter(
          (m) => !existingIds.has(m.id)
        );
        return [...prev, ...newUniqueMovies];
      });
    } else {
      setMovies(data.movies || []);
    }
  }, [data, isMobile, page]);

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
    if (!isFetching && hasMore) {
      setPage((prev) => prev + 1);
    }
  }, [isFetching, hasMore]);

  const handleDeleteClick = async (movieId: number) => {
    try {
      await deleteMovie(movieId);
      setMovies((prev) => prev.filter((m) => m.id !== movieId));
    } catch (err) {
      console.error("Failed to delete movie: ", err);
    }
  };

  if (isLoading && page === 1) return <p>Loading...</p>;
  if (error) return <p>Error loading movies.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-center pt-4 pb-24 w-full">
      <AdminMovieList
        movies={movies}
        totalPages={totalPages}
        page={page}
        handleNextPage={handleNextPage}
        handlePrevPage={handlePrevPage}
        onPageChange={onPageChange}
        hasMore={hasMore}
        loadMore={loadMore}
        handleDeleteClick={handleDeleteClick}
      />
    </PageContent>
  );
};

export default EditMovies;
