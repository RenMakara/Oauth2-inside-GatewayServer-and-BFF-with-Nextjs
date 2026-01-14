/**
 * Root Layout
 * Wraps all pages with common HTML structure
 */

export const metadata = {
    title: "Microservices Dashboard",
    description: "Demo showcasing Microservices, API Gateway, and BFF pattern",
};

export default function RootLayout({
                                       children,
                                   }: {
    children: React.ReactNode;
}) {
    return (
        <html lang="en">
        <body style={{ margin: 0, padding: 0 }}>{children}</body>
        </html>
    );
}