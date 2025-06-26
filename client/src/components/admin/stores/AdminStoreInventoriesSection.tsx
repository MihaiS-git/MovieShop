import { useLazyGetInventoriesByStoreIdQuery } from "@/features/inventories/inventoryApi";
import { useEffect, useRef, useState } from "react";
import InventoriesFiltersBlock from "./inventories/InventoriesFiltersBlock";
import AdminStoreInventoriesList from "./inventories/AdminStoreInventoriesList";
import ListItemsPagination from "@/components/ui/ListItemsPagination";
import { useIsMobile } from "@/hooks/useIsMobile";
import { InventoryItem } from "@/types/Inventory";
import useContainerInfiniteScroll from "@/hooks/useContainerInfiniteScroll";

const AdminStoreInventoriesSection = ({ storeId }: { storeId: number }) => {
  const isMobile = useIsMobile();

  const [inventories, setInventories] = useState<InventoryItem[]>([]);
  const [orderBy, setOrderBy] = useState("id_asc");
  const [page, setPage] = useState(1);
  const limit = 20;

  const [
    fetchInventories,
    { data: inventoriesData, isLoading, isFetching, error },
  ] = useLazyGetInventoriesByStoreIdQuery();

  useEffect(() => {
    setPage(1);
  }, [orderBy]);

  useEffect(() => {
    if (!inventoriesData || !inventoriesData.inventories) return;

    setInventories((prev) => {
      if (page === 1) return inventoriesData.inventories;

      const prevIds = new Set(prev.map((i) => i.id));
      const newInventories = inventoriesData.inventories.filter(
        (i) => !prevIds.has(i.id)
      );

      if (newInventories.length === 0) return prev;

      return isMobile ? [...prev, ...newInventories] : [...newInventories];
    });
  }, [inventoriesData, isMobile, page]);

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

  const hasMore = totalPages ? page < totalPages : false;

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
    if (isLoading || isFetching || !hasMore) return;
    setPage((prev) => prev + 1);
  };

  const resetAllFilters = () => {
    setOrderBy("id_asc");
    setPage(1);
  };

  const containerRef = useRef<HTMLDivElement | null>(null);
  const loaderRef = useContainerInfiniteScroll(loadMore, hasMore, containerRef);

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

        <div
          ref={containerRef}
          className="overflow-y-auto max-h-[600px] w-full"
        >
          <AdminStoreInventoriesList inventories={inventories} />

          {isMobile ? (
            hasMore ? (
              <div
                ref={loaderRef}
                className="mt-6 h-16 text-sm text-charcoal-800 flex items-center justify-center"
                aria-busy="true"
                aria-live="polite"
              >
                <div role="status">
                  <span className="sr-only">Loading more inventories...</span>
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
                No more inventories.
              </div>
            )
          ) : (
            totalPages &&
            totalPages > 1 && (
              <ListItemsPagination listItemsPagination={listItemsPagination} />
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminStoreInventoriesSection;
