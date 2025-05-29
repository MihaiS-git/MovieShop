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
  searchTerm: string;
  handleSearchTermChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
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
  searchTerm,
  handleSearchTermChange,
}: MovieListFiltersBlockProps) => {
  return (
    <div className="w-full mb-4 rounded-lg bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-sm">
      
        <div className="w-full grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-10 p-2 gap-1 sm:gap-x-5 md:gap-x-10 md:px-12 lg:gap-x-2 lg:px-2">
          <div className="flex flex-row items-center justify-start cols-span-1 lg:col-span-2 mx-auto">
            <label htmlFor="searchMovie" className="w-15 text-start">Search: </label>
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
            <label htmlFor="orderBy" className="w-15 text-start">Sort by:</label>
            <select
              name="orderBy"
              id="orderBy"
              value={orderBy}
              className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35"
              onChange={(e) => setOrderBy(e.target.value)}
            >
              <option value="None">None</option>
              <option value="Title Ascending">Title Ascending</option>
              <option value="Title Descending">Title Descending</option>
              <option value="Rating Ascending">Rating Ascending</option>
              <option value="Rating Descending">Rating Descending</option>
            </select>
          </div>

          <div className="flex flex-row items-center justify-start col-span-1 xl:col-span-2 mx-auto">
            <label htmlFor="ratingFilter" className="w-15 text-start">Rating: </label>
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
            <label htmlFor="categoryFilter" className="w-15 text-start">Category: </label>
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
            <label htmlFor="ratingFilter" className="w-15 text-start">Year: </label>
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
        </div>

    </div>
  );
};

export default MovieListFiltersBlock;
