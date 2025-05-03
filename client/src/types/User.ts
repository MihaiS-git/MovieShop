export interface User {
  email: string;
  name: string;
  role: string;
  picture: string;
}

export interface AuthResponse {
  token: string | null;
  user: User | null;
}
