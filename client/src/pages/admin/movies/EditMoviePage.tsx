import { useLazySearchActorsByNameQuery } from "@/features/actors/actorApi";
import {
  useAddActorToMovieMutation,
  useAddCategoryToMovieMutation,
  useGetMovieByIdQuery,
  useRemoveActorFromMovieMutation,
  useRemoveCategoryFromMovieMutation,
  useUpdateMovieMutation,
} from "@/features/movies/movieApi";
import PageContent from "@/PageContent";
import { Actor } from "@/types/Actor";
import { CATEGORIES, Language, LANGUAGES, Rating } from "@/types/Movie";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { CiSquareMinus, CiSquarePlus } from "react-icons/ci";

const MIN_RELEASE_YEAR = 1930;

const EditMoviePage = () => {
  const { id } = useParams();
  const movieId = Number(id);

  const [updateMovie, { isLoading: isUpdating }] = useUpdateMovieMutation();

  const { data, isLoading, error } = useGetMovieByIdQuery(movieId, {
    refetchOnMountOrArgChange: true,
  });
  const movie = data;
  const movieCategories = movie?.categories;

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [language, setLanguage] = useState<Language>(LANGUAGES[0] as Language);
  const [length, setLength] = useState<number>(0);
  const [originalLanguage, setOriginalLanguage] = useState<
    Language | undefined
  >(undefined);
  const [rating, setRating] = useState<Rating>(Rating.R);
  const [releaseYear, setReleaseYear] = useState<number>(0);
  const [rentalDuration, setRentalDuration] = useState<number>(0);
  const [rentalRate, setRentalRate] = useState<number>(0);
  const [replacementCost, setReplacementCost] = useState<number>(0);

  const [selectedCategory, setSelectedCategory] = useState("");

  const [searchName, setSearchName] = useState("");
  const [actorsResult, setActorsResult] = useState<Actor[]>([]);

  const [
    triggerSearch,
    { data: actorsData, isLoading: actorsLoading, error: actorsError },
  ] = useLazySearchActorsByNameQuery();

  const [addCategoryToMovie] = useAddCategoryToMovieMutation();
  const [removeCategoryFromMovie] = useRemoveCategoryFromMovieMutation();
  const [addActorToMovie] = useAddActorToMovieMutation();
  const [removeActorFromMovie] = useRemoveActorFromMovieMutation();

  useEffect(() => {
    if (movie) {
      setTitle(movie.title);
      setDescription(movie.description);
      setLanguage(movie.language as Language);
      setLength(movie.length);
      setOriginalLanguage(movie.originalLanguage as Language);
      setRating(movie.rating);
      setReleaseYear(movie.releaseYear);
      setRentalDuration(movie.rentalDuration);
      setRentalRate(movie.rentalRate);
      setReplacementCost(movie.replacementCost);
    }
  }, [movie]);

  useEffect(() => {
    if (actorsLoading || actorsError) return;
    if (actorsData) setActorsResult(actorsData);
  }, [actorsData, actorsLoading, actorsError]);

  const handleChangeTitle = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  const handleChangeDescription = (
    e: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    setDescription(e.target.value);
  };

  const handleChangeLanguage = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setLanguage(e.target.value as Language);
  };

  const handleChangeLength = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLength(Number(e.target.value));
  };

  const handleChangeOriginalLanguage = (
    e: React.ChangeEvent<HTMLSelectElement>
  ) => {
    const value = e.target.value;
    if (value === "") {
      setOriginalLanguage(undefined);
    } else if (LANGUAGES.includes(value as Language)) {
      setOriginalLanguage(value as Language);
    }
  };

  const handleRatingChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setRating(Rating[e.target.value as keyof typeof Rating]);
  };

  const handleChangeReleaseYear = (e: React.ChangeEvent<HTMLInputElement>) => {
    setReleaseYear(Number(e.target.value));
  };

  const handleChangeRentalDuration = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setRentalDuration(Number(e.target.value));
  };

  const handleChangeRentalRate = (e: React.ChangeEvent<HTMLInputElement>) => {
    setRentalRate(Number(e.target.value));
  };

  const handleChangeReplacementCost = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setReplacementCost(Number(e.target.value));
  };

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!movieId) return;

    if (!title.trim()) {
      alert("Title is required");
      return;
    }

    if (!description.trim()) {
      alert("Description is required");
      return;
    }

    if (
      releaseYear < MIN_RELEASE_YEAR ||
      releaseYear > new Date().getFullYear()
    ) {
      alert("Release year is not valid");
      return;
    }

    if (!language) {
      alert("Language must have a value set.");
      return;
    }

    if (
      length < 0 ||
      rentalDuration < 0 ||
      rentalRate < 0 ||
      replacementCost < 0
    ) {
      alert("Numeric fields must not be negative.");
      return;
    }

    try {
      await updateMovie({
        id: movieId,
        data: {
          title,
          description,
          language,
          length,
          originalLanguage,
          rating,
          releaseYear,
          rentalDuration,
          rentalRate,
          replacementCost,
          lastUpdate: new Date().toISOString(),
        },
      }).unwrap();
      alert("Movie updated successfully.");
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : "Unknown error";
      alert(`Update failed: ${message}`);
    }
  };

  const handleSelectedCategoryChange = (
    e: React.ChangeEvent<HTMLSelectElement>
  ) => {
    if (e.target.value !== "None") {
      setSelectedCategory(e.target.value);
    } else {
      setSelectedCategory("");
    }
  };

  const handleAddCategory = async (category: string) => {
    if (category !== "") {
      await addCategoryToMovie({ id: movie!.id, category });
    } else {
      alert("Please select a valid category.");
    }
  };

  const handleDeleteCategory = async (category: string) => {
    await removeCategoryFromMovie({ id: movie!.id, category });
  };

  const handleAddActor = async (actorId: number) => {
    await addActorToMovie({ id: movie!.id, actorId });
  };

  const handleDeleteActor = async (actorId: number) => {
    await removeActorFromMovie({ id: movie!.id, actorId });
  };

  const handleSearchNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchName(e.target.value);
  };

  const handleSearchActor = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!searchName.trim()) return;
    triggerSearch(searchName);
  };

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error loading movie.</p>;

  return (
    <PageContent className="flex flex-col items-center justify-items-center 2xl:justify-items-start pt-4 w-full min-h-screen text-charcoal-800 2xl:pb-56">
      <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 p-2 mb-4 rounded-2xl">
        Edit Movie
      </h1>
      <div className="flex flex-col items-start justify-evenly gap-4 2xl:flex-row mb-24 w-full sm:w-8/12 lg:w-1/2 2xl:w-11/12 bg-gray-200 p-2">
        <img
          src="/movie_placeholder.png"
          alt="Movie Poster"
          className="h-120 lg:h-160 mb-0 shadow-charcoal-800 shadow-[8px_8px_10px_-3px_rgba(0,0,0,0.3)] 2xl:w-1/3 2xl:h-full mx-auto"
        />

        <div className="flex flex-col text-sm 2xl:w-1/3">
          <div className="flex flex-col justify-between text-sm border border-charcoal-800 rounded-sm p-2 mb-2">
            <form onSubmit={handleFormSubmit}>
              <h3 className="bg-gray-300 p-2 font-semibold my-1">
                GENERAL INFORMATIONS
              </h3>
              <div className="my-1">
                <label htmlFor="title">
                  Title:{" "}
                  <input
                    id="title"
                    name="title"
                    type="text"
                    value={title}
                    onChange={handleChangeTitle}
                    className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full my-1"
                    minLength={1}
                    required
                  />
                </label>
              </div>
              <div className="my-1">
                <label htmlFor="description">
                  Description:{" "}
                  <textarea
                    name="description"
                    id="description"
                    value={description}
                    onChange={handleChangeDescription}
                    className="h-15 bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
                    minLength={1}
                    required
                  ></textarea>
                </label>
              </div>
              <div className="my-1 flex flex-row justify-between">
                <label htmlFor="language">Language: </label>
                <select
                  name="language"
                  id="language"
                  onChange={handleChangeLanguage}
                  value={language}
                  className="w-4/7 bg-gray-300 rounded-sm p-1 border-charcoal-800"
                  required
                >
                  <option value="" disabled>
                    Select Language
                  </option>
                  {LANGUAGES.map((lang) => (
                    <option key={lang} value={lang}>
                      {lang}
                    </option>
                  ))}
                </select>
              </div>
              <div className="my-1 flex flex-row justify-between">
                <label htmlFor="originalLanguage">Original language: </label>
                <select
                  name="originalLanguage"
                  id="originalLanguage"
                  onChange={handleChangeOriginalLanguage}
                  value={originalLanguage ?? ""}
                  className="w-4/7 bg-gray-300 rounded-sm p-1 border-charcoal-800"
                >
                  <option value="">Select Language</option>
                  {LANGUAGES.map((lang) => (
                    <option key={lang} value={lang}>
                      {lang}
                    </option>
                  ))}
                </select>
              </div>
              <div className="my-1">
                <label htmlFor="length">
                  Length:{" "}
                  <input
                    id="length"
                    name="length"
                    type="number"
                    value={length}
                    onChange={handleChangeLength}
                    className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
                    min={1}
                    required
                  />
                </label>
              </div>
              <div className="my-1">
                <label htmlFor="rating">
                  Rating:{" "}
                  <select
                    name="rating"
                    id="rating"
                    className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
                    value={rating}
                    onChange={handleRatingChange}
                    required
                  >
                    <option value="G">G</option>
                    <option value="PG">PG</option>
                    <option value="PG13">PG-13</option>
                    <option value="R">R</option>
                    <option value="NC17">NC-17</option>
                  </select>
                </label>
              </div>
              <div className="my-1">
                <label htmlFor="releaseYear">
                  Release Year:{" "}
                  <input
                    id="releaseYear"
                    name="releaseYear"
                    type="number"
                    value={releaseYear}
                    onChange={handleChangeReleaseYear}
                    className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
                    min={MIN_RELEASE_YEAR}
                    max={Number(new Date().getFullYear())}
                    required
                  />
                </label>
              </div>
              <div className="my-1">
                <label htmlFor="rentalDuration">
                  Rental Duration:{" "}
                  <input
                    id="rentalDuration"
                    name="rentalDuration"
                    type="number"
                    value={rentalDuration}
                    onChange={handleChangeRentalDuration}
                    className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
                    min={1}
                    step="1"
                    required
                  />
                </label>
              </div>
              <div className="my-1">
                <label htmlFor="rentalRate">
                  Rental Rate:{" "}
                  <input
                    id="rentalRate"
                    name="rentalRate"
                    type="number"
                    value={rentalRate}
                    onChange={handleChangeRentalRate}
                    className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
                    min={0.0}
                    required
                    step="0.01"
                  />
                </label>
              </div>
              <div className="my-1">
                <label htmlFor="replacementCost">
                  Replacement Cost:{" "}
                  <input
                    id="replacementCost"
                    name="replacementCost"
                    type="number"
                    value={replacementCost}
                    onChange={handleChangeReplacementCost}
                    className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 w-full"
                    min={0.0}
                    required
                    step="0.01"
                  />
                </label>
              </div>
              <div className="flex flex-col items-center">
                <button
                  type="submit"
                  className="bg-green-600 hover:bg-green-500 rounded-sm mt-2 p-1 cursor-pointer px-15"
                  disabled={isUpdating}
                >
                  {isUpdating ? "Updating" : "Save changes"}
                </button>
              </div>
            </form>
          </div>

          <div className="flex flex-col gap-0.75 text-sm border border-charcoal-800 rounded-sm p-2 w-full 2xl:h-full">
            <h3 className="bg-gray-300 p-2 text-sm mb-2 font-semibold">
              CATEGORIES
            </h3>
            <div className="w-full flex flex-row justify-between">
              <ul
                id="categoriesList"
                className="flex flex-col items-stretch bg-gray-300 w-full"
              >
                {movie?.categories.map((category) => (
                  <li
                    key={category}
                    value={category}
                    className="w-full flex flex-row justify-between items-center px-2 m-1"
                  >
                    <p>{category}</p>
                    <button onClick={() => handleDeleteCategory(category)}>
                      <CiSquareMinus className="text-red-700 size-5 hover:text-red-400 cursor-pointer" />
                    </button>
                  </li>
                ))}
              </ul>
            </div>
            <hr className="border-charcoal-800 my-2" />
            <form
              onSubmit={(e) => {
                e.preventDefault();
                handleAddCategory(selectedCategory);
              }}
              className="flex flex-row justify-between"
            >
              <select
                name="newCategory"
                id="newCategory"
                className="bg-gray-300 w-full me-2"
                onChange={handleSelectedCategoryChange}
              >
                <option value="None">None</option>
                {CATEGORIES.filter(
                  (category) => !movieCategories?.includes(category)
                ).map((category) => (
                  <option key={category} value={category}>
                    {category}
                  </option>
                ))}
              </select>
              <button>
                <CiSquarePlus className="text-green-700 size-5 hover:text-green-400 cursor-pointer" />
              </button>
            </form>
          </div>
        </div>

        <div className="flex flex-col gap-2 text-sm border border-charcoal-800 rounded-sm p-2 w-full 2xl:w-1/3">
          <h3 className="bg-gray-300 p-2 text-sm mb-2 font-semibold">ACTORS</h3>
          <ul>
            {movie?.actors.map((actor) => (
              <li key={actor.id} className="flex flex-row justify-between m-1">
                <p>
                  {actor.firstName} {actor.lastName}
                </p>
                <button onClick={() => handleDeleteActor(actor.id)}>
                  <CiSquareMinus className="text-red-700 size-5 hover:text-red-400 cursor-pointer" />
                </button>
              </li>
            ))}
          </ul>
          <hr className="border-charcoal-800 my-2" />
          <div>
            <form
              className="flex flex-row justify-between items-center"
              onSubmit={handleSearchActor}
            >
              <label htmlFor="searchActor" className="w-4/5 me-2">
                <input
                  id="searchActor"
                  type="text"
                  name="searchName"
                  className="bg-gray-300 border border-charcoal-800 rounded-sm p-1 px-2 w-full"
                  placeholder="Search actor by name..."
                  onChange={handleSearchNameChange}
                />
              </label>
              <button
                type="submit"
                className="bg-green-600 hover:bg-green-500 rounded-sm p-1 w-1/5 cursor-pointer"
              >
                Search
              </button>
            </form>
            {actorsResult.length > 0 && (
              <>
                <h4 className="text-base font-semibold mt-4">Search Result:</h4>
                <ul>
                  {actorsResult.map((actor) => (
                    <li
                      key={actor.id}
                      className="flex flex-row justify-between m-1"
                    >
                      <p>
                        {actor.firstName} {actor.lastName}
                      </p>
                      <button onClick={() => handleAddActor(actor.id)}>
                        <CiSquarePlus className="text-green-700 size-5 hover:text-green-400 cursor-pointer" />
                      </button>
                    </li>
                  ))}
                </ul>
              </>
            )}
          </div>
        </div>
      </div>
    </PageContent>
  );
};

export default EditMoviePage;
