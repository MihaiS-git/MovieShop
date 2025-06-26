import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';

import type { EnhancedStore } from '@reduxjs/toolkit';
import { actorApi } from '@/features/actors/actorApi';
import { userApi } from '@/features/users/userApi';
import { movieApi } from "../features/movies/movieApi";
import { addressApi } from '@/features/addresses/addressApi';
import { countryApi } from '@/features/countries/countryApi';
import { storeApi } from '@/features/stores/storeApi';
import { inventoryApi } from '@/features/inventories/inventoryApi';

export const store: EnhancedStore = configureStore({
  reducer: {
    auth: authReducer,
    [movieApi.reducerPath]: movieApi.reducer,
    [actorApi.reducerPath]: actorApi.reducer,
    [userApi.reducerPath]: userApi.reducer,
    [addressApi.reducerPath]: addressApi.reducer,
    [countryApi.reducerPath]: countryApi.reducer,
    [storeApi.reducerPath]: storeApi.reducer,
    [inventoryApi.reducerPath]: inventoryApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
  .concat(movieApi.middleware)
  .concat(actorApi.middleware)
  .concat(userApi.middleware)
  .concat(addressApi.middleware)
  .concat(countryApi.middleware)
  .concat(storeApi.middleware)
  .concat(inventoryApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export { movieApi, actorApi, userApi, addressApi, countryApi, storeApi, inventoryApi };
