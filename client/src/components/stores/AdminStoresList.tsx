import { useGetStoresQuery } from "@/features/stores/storeApi";
import { StoreItem } from "@/types/Store";
import { useEffect, useState } from "react";
import AdminStoreCard from "./AdminStoreCard";
import { useIsMobile } from "@/hooks/useIsMobile";
import StoresListFiltersBlock from "./StoresListFiltersBlock";
import useInfiniteScroll from "@/hooks/useInfiniteScroll";
import ListItemsPagination from "../ui/ListItemsPagination";
import { ItemsPagination } from "@/types/ItemsPagination";

const AdminStoresList = () => {
  const limit = 20;
  const isMobile = useIsMobile();

  const [page, setPage] = useState(1);
  const [orderBy, setOrderBy] = useState("None");
  const [countryFilter, setCountryFilter] = useState("");
  const [debounceCountry, setDebounceCountry] = useState(countryFilter);
  const [cityFilter, setCityFilter] = useState("");
  const [debounceCity, setDebounceCity] = useState(cityFilter);

  const [stores, setStores] = useState<StoreItem[]>([]);
  const { data, isLoading, isFetching, error } = useGetStoresQuery({
    page: page - 1,
    limit,
    orderBy,
    countryFilter:
      debounceCity.trim() === "" ? undefined : debounceCountry.trim(),
    cityFilter: debounceCity.trim() === "" ? undefined : debounceCity.trim(),
  });

  const totalCount = data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / limit);
  const hasMore = page < totalPages;

  useEffect(() => {
    const timeout = setTimeout(() => {
      setDebounceCountry(countryFilter);
      setDebounceCity(cityFilter);
    }, 300);

    return () => clearTimeout(timeout);
  }, [countryFilter, cityFilter]);

  useEffect(() => {
    setPage(1);
  }, [orderBy, debounceCountry, debounceCity]);

  useEffect(() => {
    if (!data || !data.stores) return;

    setStores((prev) => {
      if (page === 1) return data.stores;

      const prevIds = new Set(prev.map((s) => s.id));
      const newStores = data.stores.filter((s) => !prevIds.has(s.id));

      if (newStores.length === 0) return prev;

      return isMobile ? [...prev, ...newStores] : [...newStores];
    });
  }, [data, isMobile, page]);

  const resetAllFilters = () => {
    setOrderBy("None");
    setCountryFilter("All");
    setCityFilter("All");
    setPage(1);
  };

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

  const loadMore = () => {
    if (page === 1 && (isLoading || isFetching)) return;

    if (!isLoading && !isFetching && hasMore) {
      setPage((prev) => prev + 1);
    }
  };

  const loaderRef = useInfiniteScroll(loadMore, hasMore);
  
  const storePagination : ItemsPagination = {totalPages, page, handleNextPage, handlePrevPage, onPageChange, hasMore, loadMore};

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error occurred loading stores list.</p>;  

  return (
    <div className="flex flex-col justify-around align-middle text-center px-4">
      <div className="w-full h-full text-red-500 dark:text-charcoal-800 py-4 flex flex-col items-center">
        <h1 className="bg-charcoal-800 dark:bg-red-500 text-base lg:text-lg text-center p-2 rounded-2xl w-50">
          Stores List
        </h1>
      </div>

      <StoresListFiltersBlock
        setPage={setPage}
        countryFilter={countryFilter}
        setCountryFilter={setCountryFilter}
        cityFilter={cityFilter}
        setCityFilter={setCityFilter}
        orderBy={orderBy}
        setOrderBy={setOrderBy}
        resetAllFilters={resetAllFilters}
      />

      <div className="grid grid-cols-6 items-center h-12 w-full text-charcoal-800 text-xs font-bold bg-gray-200 dark:bg-gray-900 dark:text-gray-200 dark:border-gray-200 cursor-pointer my-0.25">
        <p>ID</p>
        <p className="col-span-2 line-clamp-3">Address</p>
        <p className="col-span-2 line-clamp-3">Manager</p>
        <p>Actions</p>
      </div>
      {stores.length > 0 ? (
        <ul>
          {stores.map((store) => (
            <li key={store.id}>
              <AdminStoreCard store={store} />
            </li>
          ))}
        </ul>
      ) : (
        <p>No stores found</p>
      )}

      {isMobile ? (
        hasMore ? (
          <div
            ref={loaderRef}
            className="mt-6 h-16 text-sm text-charcoal-800 flex items-center justify-center"
          >
            <div role="status">
              <svg
                aria-hidden="true"
                className="w-8 h-8 text-gray-200 animate-spin dark:text-gray-600 fill-charcoal-800 dark:fill-red-600"
                viewBox="0 0 100 101"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                  fill="currentColor"
                />
                <path
                  d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                  fill="currentFill"
                />
              </svg>
              <span className="sr-only">Loading...</span>
            </div>
          </div>
        ) : (
          <div className="mt-6 text-charcoal-800 text-sm">No more movies.</div>
        )
      ) : (
        totalPages > 1 && (
          <ListItemsPagination listItemsPagination={storePagination} />
        )
      )}
    </div>
  );
};

export default AdminStoresList;
