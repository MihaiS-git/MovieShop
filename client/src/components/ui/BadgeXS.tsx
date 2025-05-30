type BadgeProps = {
  element: string;
};

const BadgeXS = ({element}: BadgeProps) => {
    return <span className="m-0.5 py-2 px-8 bg-charcoal-800 text-red-500 dark:bg-red-500 dark:text-charcoal-800 text-xs rounded-2xl">
              {element}
            </span>;
};

export default BadgeXS;