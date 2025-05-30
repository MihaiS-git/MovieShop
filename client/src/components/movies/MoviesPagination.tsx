import React from "react";
import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
  } from "../ui/pagination";
import { MoviePagination } from "@/types/MovieFilters";
  
  type Props = {
    moviePagination: MoviePagination
  };
  
  const MoviesPagination: React.FC<Props> = ({ moviePagination }) => {
    const { page, totalPages, onPageChange, handlePrevPage, handleNextPage } = moviePagination;
  
    const getPageNumbers = () => {
      const pages = new Set<number>();
  
      pages.add(1);
      pages.add(2);
      pages.add(totalPages - 1);
      pages.add(totalPages);
  
      if (page > 2 && page < totalPages - 1) {
        pages.add(page - 1);
        pages.add(page);
        pages.add(page + 1);
      }
  
      return Array.from(pages)
        .filter(p => p >= 1 && p <= totalPages)
        .sort((a, b) => a - b);
    };
  
    const pages = getPageNumbers();
  
    return (
      <div className="mt-6 flex justify-center">
        <Pagination>
          <PaginationContent>
            <PaginationItem>
              <PaginationPrevious
                onClick={handlePrevPage}
                className={page === 1 ? "pointer-events-none opacity-50" : ""}
              />
            </PaginationItem>
  
            {pages.map((p, idx) => (
              <React.Fragment key={p}>
                {idx > 0 && p - pages[idx - 1] > 1 && (
                  <PaginationItem>
                    <PaginationEllipsis />
                  </PaginationItem>
                )}
                <PaginationItem>
                  <PaginationLink
                    isActive={p === page}
                    onClick={() => onPageChange(p)}
                  >
                    {p}
                  </PaginationLink>
                </PaginationItem>
              </React.Fragment>
            ))}
  
            <PaginationItem>
              <PaginationNext
                onClick={handleNextPage}
                className={page === totalPages ? "pointer-events-none opacity-50" : ""}
              />
            </PaginationItem>
          </PaginationContent>
        </Pagination>
      </div>
    );
  };
  
  export default MoviesPagination;
  