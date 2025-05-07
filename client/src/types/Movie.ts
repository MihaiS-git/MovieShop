export enum Rating {
  G = "G",
  PG = "PG",
  PG13 = "PG-13",
  R = "R",
  NC17 = "NC-17",
}

interface Language {
    id: number;
    name: string;
}

export interface Movie {
  id: number;
  title: string;
  description: string;
  releaseYear: number;
  languageId: number;
  originalLanguageId?: number;
  rentalDuration: number;
  rentalRate: number;
  length: number;
  replacementCost: number;
  rating: Rating;
  lastUpdate: string;

  // hydrated fields    
  language?: Language;
  originalLanguage?: Language;
}
