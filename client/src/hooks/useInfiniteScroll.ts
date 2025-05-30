import { useEffect, useRef } from "react";

const useInfiniteScroll = (callback: () => void, hasMore: boolean) => {
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!hasMore) return;
    const observer = new IntersectionObserver(
      (entries) => entries[0].isIntersecting && callback(),
      { threshold: 1.0 }
    );
    const el = ref.current;
    if (el) observer.observe(el);
    
    return () => {
      if (el) observer.unobserve(el);
    };
  }, [hasMore, callback]);

  return ref;
};

export default useInfiniteScroll;