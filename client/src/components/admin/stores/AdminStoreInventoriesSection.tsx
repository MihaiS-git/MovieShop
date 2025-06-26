import { useLazyGetInventoriesByStoreIdQuery } from "@/features/inventories/inventoryApi";
import { useEffect, useState } from "react";
import InventoriesFiltersBlock from "./inventories/InventoriesFiltersBlock";
import AdminStoreInventoriesList from "./inventories/AdminStoreInventoriesList";
import ListItemsPagination from "@/components/ui/ListItemsPagination";

const AdminStoreInventoriesSection = ({ storeId }: { storeId: number }) => {
  const [orderBy, setOrderBy] = useState("id_asc");
  const [page, setPage] = useState(1);
  const limit = 20;

  const [
    fetchInventories,
    { data: inventoriesData, isLoading, isFetching, error },
  ] = useLazyGetInventoriesByStoreIdQuery();

  useEffect(() => {
    fetchInventories({
      storeId,
      page: page - 1,
      limit,
      orderBy,
    });
  }, [storeId, page, orderBy]);

  const totalPages = inventoriesData?.totalPages;
  const isLastPage = inventoriesData?.isLastPage;
  const fetchedPage = inventoriesData?.currentPage;

  const hasMore = fetchedPage && totalPages ? fetchedPage < totalPages : false;

  const handleNextPage = () => {
    if (totalPages !== undefined && page < totalPages && !isLastPage) {
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

  const loadMore = () => {
    // Prevent triggering while first page is still loading
    if (page === 1 && (isLoading || isFetching)) return;

    if (!isLoading && !isFetching && hasMore) {
      setPage((prev) => prev + 1);
    }
  };

  const resetAllFilters = () => {
    setOrderBy("id_asc");
    setPage(1);
  };

  const listItemsPagination = {
    page,
    totalPages: inventoriesData?.totalPages ?? 1,
    onPageChange,
    handlePrevPage,
    handleNextPage,
    hasMore,
    loadMore,
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading inventories data.</p>;

  return (
    <div className="flex flex-col items-center 2xl:items-start justify-evenly gap-2 mb-12 w-full sm:w-8/12 lg:w-1/2 2xl:w-11/12 bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600 text-xs md:text-base">
      <div className="flex flex-col justify-center items-center w-full bg-gray-200 dark:bg-charcoal-800 text-charcoal-800 dark:text-gray-200 p-2 border border-charcoal-800 dark:border-gray-600">
        <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 py-1 px-2 mb-2 rounded-xs">
          Store #{storeId} Inventory
        </h1>

        <InventoriesFiltersBlock
          orderBy={orderBy}
          setOrderBy={setOrderBy}
          resetAllFilters={resetAllFilters}
        />

        <div className="grid grid-cols-8 items-center h-12 w-full text-charcoal-800 text-xs font-bold bg-gray-200 dark:bg-gray-900 dark:text-gray-200 dark:border-gray-200 cursor-pointer my-0.25 text-center">
          <p>ID</p>
          <p>Store ID</p>
          <p className="col-span-2">Movie</p>
          <p>Year</p>
          <p>Rate</p>
          <p>Cost</p>
          <p>Actions</p>
        </div>

        <AdminStoreInventoriesList inventories={inventoriesData?.inventories} />

        <ListItemsPagination listItemsPagination={listItemsPagination} />
      </div>
    </div>
  );
};

export default AdminStoreInventoriesSection;
