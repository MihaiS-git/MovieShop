import { CATEGORIES, RATING, YEARS } from "@/types/Movie";

type MovieListFiltersBlockProps = {
  orderBy: string;
  setOrderBy: (value: string) => void;
  ratingFilter: string;
  setRatingFilter: (value: string) => void;
  yearFilter: number;
  setYearFilter: (value: number) => void;
  categoryFilter: string;
  setCategoryFilter: (category: string) => void;
};

const MovieListFiltersBlock = ({
  orderBy,
  setOrderBy,
  ratingFilter,
  setRatingFilter,
  yearFilter,
  setYearFilter,
  categoryFilter,
  setCategoryFilter,
}: MovieListFiltersBlockProps) => {
  return (
    <div className="w-full mb-4 rounded-lg bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-sm">
      <form>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 p-4 gap-2 sm:gap-x-10 md:gap-x-20 md:px-24 lg:gap-x-12 lg:px-8">
          <div className="flex flex-row items-center justify-between">
            <label htmlFor="orderBy">Sort by:</label>
            <select
              name="orderBy"
              id="orderBy"
              value={orderBy}
              className="bg-gray-200 text-charcoal-800 rounded-sm p-1 w-35 ms-2"
              onChange={(e) => setOrderBy(e.target.value)}
            >
              <option value="None">None</option>
              <option value="Title Ascending">Title Ascending</option>
              <option value="Title Descending">Title Descending</option>
              <option value="Rating Ascending">Rating Ascending</option>
              <option value="Rating Descending">Rating Descending</option>
            </select>
          </div>

          <div className="flex flex-row items-center justify-between">
            <label htmlFor="ratingFilter">Rating: </label>
            <select
              name="ratingFilter"
              id="ratingFilter"
              value={ratingFilter}
              className="bg-gray-200 text-charcoal-800 rounded-sm p-1 w-35"
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

          <div  className="flex flex-row items-center justify-between">
            <label htmlFor="categoryFilter">Category: </label>
            <select
              name="categoryFilter"
              id="categoryFilter"
              value={categoryFilter}
              className="bg-gray-200 text-charcoal-800 rounded-sm p-1 w-35"
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

          <div className="flex flex-row items-center justify-between">
            <label htmlFor="ratingFilter">Year: </label>
            <select
              name="yearFilter"
              id="yearFilter"
              value={yearFilter}
              className="bg-gray-200 text-charcoal-800 rounded-sm p-1 w-35"
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
        </div>
      </form>
    </div>
  );
};

export default MovieListFiltersBlock;
