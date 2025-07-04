import { useCreateMovieMutation } from "@/features/movies/movieApi";
import PageContent from "@/PageContent";
import { Language, LANGUAGES, Rating } from "@/types/Movie";
import { useState } from "react";

const AddMovie = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [language, setLanguage] = useState<Language>(LANGUAGES[0] as Language);
  const [length, setLength] = useState<number>(0);
  const [originalLanguage, setOriginalLanguage] = useState<Language>(LANGUAGES[0] as Language);
  const [rating, setRating] = useState<Rating>(Rating.R);
  const [releaseYear, setReleaseYear] = useState<number>(0);
  const [rentalDuration, setRentalDuration] = useState<number>(0);
  const [rentalRate, setRentalRate] = useState<number>(0);
  const [replacementCost, setReplacementCost] = useState<number>(0);

  const [createMovie] = useCreateMovieMutation();

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
    setOriginalLanguage(e.target.value as Language);
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

    if (!title.trim()) {
      alert("Title is required");
      return;
    }

    if (!description.trim()) {
      alert("Description is required");
      return;
    }

    if (releaseYear < 1930 || releaseYear > new Date().getFullYear()) {
      alert("Release year is not valid");
      return;
    }

    if (!language) {
      alert("Language must be set.");
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
      await createMovie({
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
      }).unwrap();
      alert("Movie created successfully");
    } catch (error) {
      console.error("Failed to create movie", error);
    }
  };

  return (
    <PageContent className="flex flex-col items-center justify-items-start pt-4 w-full min-h-screen text-charcoal-800">
      <h1 className="bg-charcoal-800 dark:bg-red-500 text-red-500 dark:text-charcoal-800 text-base lg:text-lg text-center w-50 p-2 mb-4 rounded-2xl">
        Add Movie
      </h1>
      <div className="flex flex-col items-center lg:flex-row pb-24 w-full justify-center gap-4">
        <form
          className=" text-sm flex flex-col gap-1 w-10/12 lg:w-full max-w-md border py-2 px-4 bg-gray-300 dark:bg-charcoal-800 border-charcoal-800 dark:border-gray-600 text-charcoal-800 dark:text-gray-200"
          onSubmit={handleFormSubmit}
        >
          <label htmlFor="title">
            Title:{" "}
            <input
              id="title"
              name="title"
              type="text"
              value={title || ""}
              onChange={handleChangeTitle}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
              min={1}
              required
            />
          </label>
          <label htmlFor="description">
            Description:{" "}
            <textarea
              name="description"
              id="description"
              value={description || ""}
              onChange={handleChangeDescription}
              className="h-15 bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
              minLength={1}
              required
            ></textarea>
          </label>
          <label htmlFor="language" className="w-full flex flex-row justify-between">
            Language:{" "}
            <select
              id="language"
              name="language"
              onChange={handleChangeLanguage}
              value={language}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-2/3"
              required
            >
              <option value="">Select language</option>
              {LANGUAGES.map((lang) => (
                <option key={lang} value={lang}>
                  {lang}
                </option>
              ))}
            </select>
          </label>
          <label htmlFor="originalLanguage" className="w-full flex flex-row justify-between">
            Original language:{" "}
            <select
              id="originalLanguage"
              name="originalLanguage"
              onChange={handleChangeOriginalLanguage}
              value={originalLanguage}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-2/3"
              required
            >
              <option value="">Select language</option>
              {LANGUAGES.map((lang) => (
                <option key={lang} value={lang}>
                  {lang}
                </option>
              ))}
            </select>
          </label>
          <label htmlFor="length">
            Length:{" "}
            <input
              id="length"
              name="length"
              type="number"
              value={length || ""}
              onChange={handleChangeLength}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
              min={1}
              required
            />
          </label>
          <label htmlFor="rating">
            Rating:{" "}
            <select
              name="rating"
              id="rating"
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
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
          <label htmlFor="releaseYear">
            Release Year:{" "}
            <input
              id="releaseYear"
              name="releaseYear"
              type="number"
              value={releaseYear || ""}
              onChange={handleChangeReleaseYear}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
              min={1930}
              max={Number(new Date().getFullYear())}
              required
            />
          </label>
          <label htmlFor="rentalDuration">
            Rental Duration:{" "}
            <input
              id="rentalDuration"
              name="rentalDuration"
              type="number"
              value={rentalDuration || ""}
              onChange={handleChangeRentalDuration}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
              min={1}
              required
            />
          </label>
          <label htmlFor="rentalRate">
            Rental Rate:{" "}
            <input
              id="rentalRate"
              name="rentalRate"
              type="number"
              value={rentalRate || ""}
              onChange={handleChangeRentalRate}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
              min={0.0}
              required
              step="0.01"
            />
          </label>
          <label htmlFor="replacementCost">
            Replacement Cost:{" "}
            <input
              id="replacementCost"
              name="replacementCost"
              type="number"
              value={replacementCost || ""}
              onChange={handleChangeReplacementCost}
              className="bg-gray-300 dark:bg-gray-700 border border-charcoal-800 rounded-sm p-1 w-full"
              min={0}
              required
              step="0.01"
            />
          </label>
          <button
            type="submit"
            className="bg-green-600 hover:bg-green-500 rounded-sm mt-2 p-1 cursor-pointer px-15"
          >
            Save Changes
          </button>
        </form>
      </div>
    </PageContent>
  );
};

export default AddMovie;
