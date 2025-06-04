import { Actor } from "./Actor";

export enum Rating {
  G = "G",
  PG = "PG",
  PG13 = "PG-13",
  R = "R",
  NC17 = "NC-17",
}

export enum Category {
  "Action",
  "Animation",
  "Children",
  "Classics",
  "Comedy",
  "Documentary",
  "Drama",
  "Family",
  "Foreign",
  "Games",
  "Horror",
  "Music",
  "New",
  "Sci-Fi",
  "Sports",
  "Travel",
}

export const RATING = ["G", "PG", "PG_13", "R", "NC_17"];

export const YEARS = [2025, 2024, 2023, 2022, 2021, 2020];

export const CATEGORIES = [  
  "Action",
  "Animation",
  "Children",
  "Classics",
  "Comedy",
  "Documentary",
  "Drama",
  "Family",
  "Foreign",
  "Games",
  "Horror",
  "Music",
  "New",
  "Sci-Fi",
  "Sports",
  "Travel",
];

export enum Language {
  ENGLISH = "English",
  FRENCH = "French",
  GERMAN = "German",
  ITALIAN = "Italian",
  JAPANESE = "Japanese",
  MANDARIN = "Mandarin",
  SPANISH = "Spanish",
  UNKNOWN = "Unknown",
}

export const LANGUAGES = [
  "English",
  "French",
  "German",
  "Italian",
  "Japanese",
  "Mandarin",
  "Spanish",
  "Unknown",
];

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
  categories: string[];
  actorIds: number[];
}

export interface MovieItem {
  id: number;
  title: string;
  description: string;
  releaseYear: number;
  language: string;
  originalLanguage: string;
  rentalRate: number;
  length: number;
  rating: Rating;
  categories: string[];
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

export interface MovieDetails {
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
  categories: string[];
  actors: Actor[];
}