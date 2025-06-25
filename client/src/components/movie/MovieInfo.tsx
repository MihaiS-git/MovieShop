import { MovieDetails } from "@/types/Movie";
import { formatCurrency } from "@/util/formatCurrency";
import React from "react";
import BadgeXS from "../ui/BadgeXS";
import { useNavigate } from "react-router-dom";

interface MovieDetailProps {
  movie: MovieDetails;
}

const MovieInfo: React.FC<MovieDetailProps> = ({ movie }) => {
  const navigate = useNavigate();

  const handleFilterClick = (
    filterKey: string,
    filterValue: string | number
  ) => {
    const params = new URLSearchParams();
    params.set(filterKey, String(filterValue));
    params.set("page", "1");

    navigate(`/movies?${params.toString()}`);
  };

  return (
    <div className="bg-red-500 dark:bg-charcoal-800 text-charcoal-800 dark:text-red-500 lg:max-w-4/5 mx-auto">
      <div className="flex flex-col xl:flex-row w-full">
        <img
          src="/movie_placeholder.png"
          alt="Movie Poster"
          className="max-w-xs mx-auto rounded-2xl shadow-red-800 dark:shadow-gold-500/50 shadow-[8px_8px_10px_-3px_rgba(0,0,0,0.3)]"
        />
        <div className="flex flex-col items-center">
          <h1 className="text-gold-500 text-2xl font-bold my-4">
            {movie.title}
          </h1>
          <p className="text-base text-left px-4 max-w-4/5 my-4">{movie.description}</p>
          <p className="text-sm text-left px-4 max-w-4/5 my-4">
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
                <span
                  className="m-0.5 py-2 px-8 bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 hover:bg-gray-300 cursor-pointer text-xs rounded-2xl"
                  onClick={() => handleFilterClick("category", category)}
                >
                  {category}
                </span>
              </li>
            ))}
          </ul>

          <div className="flex flex-row flex-wrap w-full justify-center items-center my-4">
            <span
              className="m-0.5 py-2 px-8 bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 hover:bg-gray-300 cursor-pointer text-xs rounded-2xl"
              onClick={() => handleFilterClick("rating", movie.rating)}
            >
              {movie.rating.replace(/_/g, "-")}
            </span>
            <span
              className="m-0.5 py-2 px-8 bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 hover:bg-gray-300 cursor-pointer text-xs rounded-2xl"
              onClick={() => handleFilterClick("year", movie.releaseYear)}
            >
              {movie.releaseYear.toString()}
            </span>
            <BadgeXS element={movie.language} />
            <BadgeXS element={`${movie.length} min`} />
            <BadgeXS element={formatCurrency(movie.rentalRate)} />
          </div>

          <div className="flex flex-row items-center justify-center mt-2 gap-4 px-4">
            <p className="mx-4 text-xs text-center">
              Rental duration: <br />
              {movie.rentalDuration} days
            </p>
            <p className="mx-4 text-xs text-center">
              Replacement cost: <br />
              {formatCurrency(movie.replacementCost)}
            </p>
          </div>

          <div className="flex flex-col md:flex-col justify-center items-center mx-auto">
            <h1 className="text-gold-500 text-xl font-bold my-2">Stars</h1>
            <ul className="flex flex-wrap justify-around gap-2 max-w-4/5">
              {movie?.actors?.map((actor) => (
                <li key={actor.id}>
                  <p className="bg-charcoal-800 text-red-500 rounded-sm p-2 text-center text-xs">
                    {actor.firstName} {actor.lastName}
                  </p>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MovieInfo;
