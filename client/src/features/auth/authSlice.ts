import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { User } from '../../types/User';

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
}

const storedAuth = localStorage.getItem('auth');

const initialState: AuthState = storedAuth
  ? JSON.parse(storedAuth)
  : {
      user: null,
      token: null,
      isAuthenticated: false,
  };

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    login(state, action: PayloadAction<{ user: User; token: string }>) {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.isAuthenticated = true;

      localStorage.setItem('auth', JSON.stringify(state));
    },
    logout(state) {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;

      localStorage.removeItem('auth');
    },
  },
});

export const { login, logout } = authSlice.actions;
export default authSlice.reducer;
