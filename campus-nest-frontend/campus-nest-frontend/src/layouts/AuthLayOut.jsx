import { Outlet, Link } from "react-router-dom";

export default function AuthLayout() {
  return (
    <>
      {/* Navbar Section */}
      <header className="w-full flex items-center justify-between px-6 py-3 bg-white shadow-md">
        <div className="flex items-center space-x-2">
          <Link to="/home" aria-label="Home">
            <img
              src="/CampusNest_final_logo1.png"
              alt="CampusNest Logo"
              className="h-25 w-40"
            />
          </Link>
        </div>
      </header>

      {/* Auth Content (Login/Signup) */}
      <main className="flex justify-center items-center min-h-[calc(80vh-80px)] bg-gray-50 p-6">
        <Outlet />
      </main>
    </>
  );
}
