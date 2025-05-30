import { Movie } from "@/types/Movie";
import { formatCurrency } from "@/util/formatcurrency";
import React from "react";

interface MovieDetailProps {
  movie: Movie;
}

const MovieDetails: React.FC<MovieDetailProps> = ({ movie }) => {
  console.log(movie);

  return (
    <div className="min-h-screen bg-red-500 dark:bg-charcoal-800 text-charcoal-800 dark:text-red-500 flex flex-col items-center gap-4">
      <h1 className="text-gold-500 text-2xl font-bold mt-2">{movie.title}</h1>
      <img
        src="/movie_placeholder.png"
        alt="Movie Poster"
        className="h-100 w-80 rounded-2xl shadow-red-800 dark:shadow-gold-500/50 shadow-[8px_8px_10px_-3px_rgba(0,0,0,0.3)]"
      />
      <ul className="flex flex-row w-full justify-center items-center mt-4">
        {movie.categories.map((category) => (
          <li key={category}>
            <span className="m-0.5 py-2 px-8 bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-xs rounded-2xl">
              {category}
            </span>
          </li>
        ))}
      </ul>
      <p className="text-lg">{movie.description}</p>
      <p className="text-center">
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab totam
        aliquid, accusamus veniam hic dolorem quasi deleniti dolores dolorum
        alias iure quam reiciendis rem quos rerum quis, excepturi necessitatibus
        saepe!
      </p>
      <div className="grid grid-cols-5 gap-1 text-charcoal-800">
        <div>
          <p className="text-center bg-gold-300 p-1 rounded-md text-xs sm:text-base">
            Rating <br /> {movie.rating}
          </p>
        </div>
        <div>
          <p className="text-center bg-gold-300 p-1 rounded-md text-xs sm:text-base">
            Year <br /> {movie.releaseYear}
          </p>
        </div>
        <div>
          <p className="text-center bg-gold-300 p-1 rounded-md text-xs sm:text-base">
            Language <br /> {movie.language}
          </p>
        </div>
        <div>
          <p className="text-center bg-gold-300 p-1 rounded-md text-xs sm:text-base">
            Length <br /> {movie.length} min
          </p>
        </div>
        <div>
          <p className="text-center bg-gold-300 p-1 rounded-md text-xs sm:text-base">
            Rental Rate <br /> {formatCurrency(movie.rentalRate)}
          </p>
        </div>
      </div>
      <div className="flex flex-col items-left mt-2 text-xs sm:text-base">
        {movie.originalLanguage !== null &&
          movie.originalLanguage !== undefined && (
            <p>Original language: {movie.originalLanguage}</p>
          )}
        <p>Rental duration: {movie.rentalDuration} days</p>
        <p>Replacement cost: {formatCurrency(movie.replacementCost)}</p>
      </div>
    </div>
  );
};

export default MovieDetails;
