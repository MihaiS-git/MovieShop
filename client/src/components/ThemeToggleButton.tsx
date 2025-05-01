import { useEffect, useState } from 'react';
import { FaMoon, FaSun } from 'react-icons/fa';

const ThemeToggleButton = () => {
  const [isDark, setIsDark] = useState(false);

  useEffect(() => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
      setIsDark(true);
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, []);

  const toggleTheme = () => {
    setIsDark(!isDark);
    if (isDark) {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('theme', 'light');
    } else {
      document.documentElement.classList.add('dark');
      localStorage.setItem('theme', 'dark');
    }
  };

  return (
    <button onClick={toggleTheme} className="p-2 mx-2 rounded-full bg-red-500 text-gray-950 transition-all duration-300 hover:bg-red-hover">
      {isDark ? (
        <FaSun size={16} />
      ) : (
        <FaMoon size={16} />
      )}
    </button>
  );
};

export default ThemeToggleButton;
