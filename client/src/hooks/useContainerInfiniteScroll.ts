import { useEffect, useRef, useCallback } from "react";

export default function useContainerInfiniteScroll(
  callback: () => void,
  hasMore: boolean,
  containerRef: React.RefObject<HTMLElement | null>
) {
  const observer = useRef<IntersectionObserver | null>(null);

  const loaderRef = useCallback(
    (node: HTMLDivElement | null) => {
      if (observer.current) observer.current.disconnect();

      if (node && containerRef.current) {
        observer.current = new IntersectionObserver(
          (entries) => {
            if (entries[0].isIntersecting && hasMore) {
              callback();
            }
          },
          {
            root: containerRef.current,
            threshold: 1.0,
          }
        );

        observer.current.observe(node);
      }
    },
    [callback, hasMore, containerRef]
  );

  useEffect(() => {
    return () => observer.current?.disconnect();
  }, []);

  return loaderRef;
}
