import { CATEGORIES, RATING, YEARS } from "@/types/Movie";
import { MovieFilters } from "@/types/MovieFilters";

type MovieListFiltersBlockProps = {
  movieFilters: MovieFilters;
};

const MovieListFiltersBlock = ({
  movieFilters,
}: MovieListFiltersBlockProps) => {
  const {
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
    resetAllFilters,
  } = movieFilters;
  return (
    <div className="w-full mb-4 rounded-lg bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-sm">
      <div className="w-full grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-11 p-2 gap-1 sm:gap-x-5 md:gap-x-10 md:px-12 lg:gap-x-2 lg:px-2">
        <div className="flex flex-row items-center justify-start cols-span-1 xl:col-span-2 mx-auto">
          <label htmlFor="searchMovie" className="w-15 text-start">
            Search:{" "}
          </label>
          <input
            type="text"
            id="searchMovie"
            name="searchMovie"
            value={searchTerm}
            onChange={handleSearchTermChange}
            className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35 ps-2"
            placeholder="Enter movie title..."
          />
        </div>

        <div className="flex flex-row items-center justify-start col-span-1 xl:col-span-2 mx-auto">
          <label htmlFor="orderBy" className="w-15 text-start">
            Sort by:
          </label>
          <select
            name="orderBy"
            id="orderBy"
            value={orderBy}
            className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35"
            onChange={(e) => setOrderBy(e.target.value)}
          >
            <option value="None">None</option>
            <option value="title_asc">Title Ascending</option>
            <option value="title_desc">Title Descending</option>
            <option value="rating_asc">Rating Ascending</option>
            <option value="rating_desc">Rating Descending</option>
          </select>
        </div>

        <div className="flex flex-row items-center justify-start col-span-1 xl:col-span-2 mx-auto">
          <label htmlFor="ratingFilter" className="w-15 text-start">
            Rating:{" "}
          </label>
          <select
            name="ratingFilter"
            id="ratingFilter"
            value={ratingFilter}
            className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35"
            onChange={(e) => setRatingFilter(e.target.value)}
          >
            <option value="All">All</option>
            {RATING.map((rating) => (
              <option key={rating} value={rating}>
                {rating.replace(/_/g, "-")}
              </option>
            ))}
          </select>
        </div>

        <div className="flex flex-row items-center justify-start col-span-1 xl:col-span-2 mx-auto">
          <label htmlFor="categoryFilter" className="w-15 text-start">
            Category:{" "}
          </label>
          <select
            name="categoryFilter"
            id="categoryFilter"
            value={categoryFilter}
            className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35"
            onChange={(e) => setCategoryFilter(e.target.value)}
          >
            <option value="All">All</option>
            {CATEGORIES.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        </div>

        <div className="flex flex-row items-center justify-start col-span-1 xl:col-span-2 mx-auto">
          <label htmlFor="ratingFilter" className="w-15 text-start">
            Year:{" "}
          </label>
          <select
            name="yearFilter"
            id="yearFilter"
            value={yearFilter}
            className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35"
            onChange={(e) => setYearFilter(Number(e.target.value))}
          >
            <option value="0">All</option>
            {YEARS.map((year) => (
              <option key={year} value={year}>
                {year}
              </option>
            ))}
            <option value={2006}>2006</option>
          </select>
        </div>
        <div className="flex flex-row items-center justify-center col-span-1 xl:col-span-1 mx-auto">
          <button
            onClick={resetAllFilters}
            className="px-2 py-1 bg-red-500 dark:bg-charcoal-800 text-charcoal-800 dark:text-red-500 rounded hover:bg-gray-500 transition text-xs"
          >
            Reset Filters
          </button>
        </div>
      </div>
    </div>
  );
};

export default MovieListFiltersBlock;
