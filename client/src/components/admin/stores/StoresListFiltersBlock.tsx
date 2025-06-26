type Props = {
  setPage: (page: number) => void;
  countryFilter: string;
  setCountryFilter: (country: string) => void;
  cityFilter: string;
  setCityFilter: (city: string) => void;
  orderBy: string;
  setOrderBy: (orderBy: string) => void;
  resetAllFilters: () => void;
};

const StoresListFiltersBlock = ({
  setPage,
  countryFilter,
  setCountryFilter,
  cityFilter,
  setCityFilter,
  orderBy,
  setOrderBy,
  resetAllFilters,
}: Props) => {
  const handleCountryFilterChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setCountryFilter(e.target.value);
    setPage(1);
  };

  const handleCityFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCityFilter(e.target.value);
    setPage(1);
  };

  return (
    <div className="w-full mb-4 rounded-lg bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-sm">
      <div className="w-full grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-11 p-2 gap-1 sm:gap-x-5 md:gap-x-10 md:px-12 lg:gap-x-2 lg:px-2">
        <div className="flex flex-row items-center justify-start cols-span-1 xl:col-span-2 mx-auto">
          <label htmlFor="countryFilter" className="w-15 text-start">
            Country:{" "}
          </label>
          <input
            type="text"
            id="countryFilter"
            name="countryFilter"
            value={countryFilter}
            onChange={handleCountryFilterChange}
            className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35 ps-2"
            placeholder="Enter country name..."
          />
        </div>

        <div className="flex flex-row items-center justify-start cols-span-1 xl:col-span-2 mx-auto">
          <label htmlFor="cityFilter" className="w-15 text-start">
            City:{" "}
          </label>
          <input
            type="text"
            id="cityFilter"
            name="cityFilter"
            value={cityFilter}
            onChange={handleCityFilterChange}
            className="bg-gray-200 border border-charcoal-800 text-charcoal-800 rounded-sm p-1 w-35 ps-2"
            placeholder="Enter country name..."
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
            <option value="id_asc">ID Ascending</option>
            <option value="id_desc">ID Descending</option>
            <option value="country_asc">Country Ascending</option>
            <option value="country_desc">Country Descending</option>
            <option value="city_asc">City Ascending</option>
            <option value="city_desc">City Descending</option>
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

export default StoresListFiltersBlock;
