import useLogout from "../features/auth/useAuth";

const LogoutButton = () => {
    const handleLogout = useLogout();
    
    return (
        <>
            <button onClick={handleLogout}>Logout</button>
        </>
    );
};

export default LogoutButton;