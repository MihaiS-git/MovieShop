import React from 'react';

const PageContent: React.FC<{ className: string, children: React.ReactNode }> = ({ className, children }) => { 
    return (
        <div className={`${className} bg-red-500 dark:bg-charcoal-800 w-full h-min-screen`}>
            {children}
        </div>
    );
}

export default PageContent;