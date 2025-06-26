type Props = {
  orderBy: string;
  setOrderBy: (value: string) => void;
  resetAllFilters: () => void;
};


const InventoriesFiltersBlock = ({orderBy, setOrderBy, resetAllFilters} : Props) => {
  return (
    <div className="w-full mb-4 rounded-lg bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-sm">
      <div className="w-full grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-11 p-2 gap-1 sm:gap-x-5 md:gap-x-10 md:px-12 lg:gap-x-2 lg:px-2">
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

export default InventoriesFiltersBlock;
