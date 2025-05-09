import { Outlet } from "react-router-dom";
import Header from "./Header";
import Footer from "./Footer";

const RootLayout = () => {
    
    return (
        <main className="flex flex-col min-h-screen font-serif overflow-y-auto bg-light-background dark:bg-dark-background text-light-text dark:bg-dark-text">
            <div className="min-h-screen w-full bg-contain">
                <Header />
                <Outlet />
                <Footer />
            </div>
        </main>
    );
}

export default RootLayout;