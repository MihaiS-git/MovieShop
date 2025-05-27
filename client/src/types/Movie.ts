export enum Rating {
  G = "G",
  PG = "PG",
  PG13 = "PG-13",
  R = "R",
  NC17 = "NC-17",
}

export enum Language {
  ENGLISH = "English",
  FRENCH = "French",
  GERMAN = "German",
  ITALIAN = "Italian",
  JAPANESE = "Japanese",
  MANDARIN = "Mandarin",
  SPANISH = "Spanish",
}

export const LANGUAGES = ["English", "French", "German", "Italian", "Japanese", "Mandarin", "Spanish"];

export interface Movie {
  id: number;
  title: string;
  description: string;
  releaseYear: number;
  language: string;
  originalLanguage?: string;
  rentalDuration: number;
  rentalRate: number;
  length: number;
  replacementCost: number;
  rating: Rating;
  lastUpdate: string;
}

export interface MovieItem {
  id: number;
  title: string;
  description: string;
  releaseYear: number;
  length: number;
  rating: Rating;
}



export interface MovieDto {
  title: string;
  description: string;
  releaseYear: number;
  language: Language;
  originalLanguage?: Language;
  rentalDuration: number;
  rentalRate: number;
  length: number;
  replacementCost: number;
  rating: Rating;
  lastUpdate: string;
}
