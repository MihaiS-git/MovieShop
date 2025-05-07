import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';
import { movieApi } from "../features/movies/movieApi";

import type { EnhancedStore } from '@reduxjs/toolkit';

export const store: EnhancedStore = configureStore({
  reducer: {
    auth: authReducer,
    [movieApi.reducerPath]: movieApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(movieApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export { movieApi };
