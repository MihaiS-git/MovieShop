import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/authSlice';
import { movieApi } from "../features/movies/movieApi";

import type { EnhancedStore } from '@reduxjs/toolkit';
import { actorApi } from '@/features/actors/actorApi';

export const store: EnhancedStore = configureStore({
  reducer: {
    auth: authReducer,
    [movieApi.reducerPath]: movieApi.reducer,
    [actorApi.reducerPath]: actorApi.reducer
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(movieApi.middleware).concat(actorApi.middleware),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export { movieApi };
