import { Movie } from "@/types/Movie";
import { formatCurrency } from "@/util/formatcurrency";
import React from "react";
import BadgeXS from "../ui/BadgeXS";

interface MovieDetailProps {
  movie: Movie;
}

const MovieDetails: React.FC<MovieDetailProps> = ({ movie }) => {
  console.log(movie);

  return (
    <div className="min-h-screen bg-red-500 dark:bg-charcoal-800 text-charcoal-800 dark:text-red-500 flex flex-col items-center gap-4 mt-0 lg:mt-8">
      <div className="flex flex-col md:flex-row justify-around items-center w-full lg:w-2/3 mx-auto">
        <img
          src="/movie_placeholder.png"
          alt="Movie Poster"
          className="h-100 w-80 mx-8 rounded-2xl shadow-red-800 dark:shadow-gold-500/50 shadow-[8px_8px_10px_-3px_rgba(0,0,0,0.3)]"
        />
        <div className="flex flex-col items-center">
          <h1 className="text-gold-500 text-2xl font-bold my-4">
            {movie.title}
          </h1>
          <p className="text-lg my-4">{movie.description}</p>
          <p className="text-sm">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab totam
            aliquid, accusamus veniam hic dolorem quasi deleniti dolores dolorum
            alias iure quam reiciendis rem quos rerum quis, excepturi
            necessitatibus saepe! Lorem, ipsum dolor sit amet consectetur
            adipisicing elit. A animi esse deleniti iusto eaque dolorum amet
            laborum vero maxime excepturi quo sequi, aliquid laudantium at rerum
            culpa sit saepe veritatis? Lorem ipsum dolor sit amet consectetur
            adipisicing elit. Impedit placeat vitae quos facere in fuga quidem
            et culpa. Doloribus, asperiores? Velit accusamus vero praesentium
            nesciunt rerum at non sunt quis?
          </p>
          <ul className="flex flex-row w-full justify-center items-center mt-4">
            {movie.categories.map((category) => (
              <li key={category}>
                <BadgeXS element={category} />
              </li>
            ))}
          </ul>

          <div className="flex flex-row flex-wrap w-full justify-center items-center mt-1.5">
            <BadgeXS element={movie.rating.replace(/_/g, "-")} />
            <BadgeXS element={movie.releaseYear.toString()} />
            <BadgeXS element={movie.language} />
            <BadgeXS element={`${movie.length} min`} />
            <BadgeXS element={formatCurrency(movie.rentalRate)} />
          </div>
          <div className="flex flex-row items-center justify-around mt-2">
            <p className="mx-4 text-xs text-center">
              Rental duration: <br />
              {movie.rentalDuration} days
            </p>
            <p className="mx-4 text-xs text-center">
              Replacement cost: <br />
              {formatCurrency(movie.replacementCost)}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MovieDetails;
