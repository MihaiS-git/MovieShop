import { useDispatch } from 'react-redux';
import { logout } from './authSlice';
import { useNavigate } from 'react-router-dom';

const useLogout = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    navigate('/signin');
  };

  return handleLogout;
};

export default useLogout;
