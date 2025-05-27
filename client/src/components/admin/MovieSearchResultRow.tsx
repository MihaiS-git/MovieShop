import { Rating } from "@/types/Movie";

interface RowProps {
  id: number | string;
  title: string;
  releaseYear: number | string;
  length: number | string;
  rating: Rating | string;
  isHeader?: boolean;
}

const MovieSearchResultRow = ({
  id,
  title,
  releaseYear,
  length,
  rating,
  isHeader = false,
}: RowProps) => {
  const baseClass = "grid grid-cols-12 gap-4 items-center px-4 py-2 h-12 text-center";
  const headerClass = "bg-gray-600 text-white font-bold";
  const rowClass = "bg-gray-400 text-gray-900 hover:bg-gray-300";

  return (
    <div className={`${baseClass} ${isHeader ? headerClass : rowClass}`}>
      <p className="col-span-1">{id}</p>
      <p className="col-span-4">{title}</p>
      <p className="col-span-3">{releaseYear}</p>
      <p className="col-span-2">{length}</p>
      <p className="col-span-2">{rating}</p>
    </div>
  );
};

export default MovieSearchResultRow;
