import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import SearchBar from "./SearchBar"; // Make sure path is correct

function NavBar() {
  const [menuOpen, setMenuOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const userId = localStorage.getItem("user");
    if (userId) {
      setIsLoggedIn(true);
      const fetchUser = async () => {
        try {
          const res = await fetch(`http://localhost:5000/api/users/${userId}`, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            credentials: "include",
          });
          const data = await res.json();
          if (res.ok) {
            setUser(data);
          }
        } catch (error) {
          console.error(error);
        }
      };
      fetchUser();
    }
  }, []);

  const handleSearch = (term) => {
    console.log("Search term:", term);
    // Add your search logic here
  };

  return (
    <header className="bg-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto flex items-center justify-between px-4 py-3 md:py-4">
        {/* Logo */}
        <div className="flex-shrink-0">
          <Link to="/home" aria-label="Home">
            <img
              src="/campusnest_logo.png"
              alt="CampusNest Logo"
              className="h-12 w-12 md:h-14 md:w-14"
            />
          </Link>
        </div>

        {/* SearchBar - Desktop */}
        <div className="hidden md:flex flex-1 justify-center px-4">
          <SearchBar onSearch={handleSearch} />
        </div>

        {/* Desktop Auth/Profile */}
        <div className="hidden md:flex items-center space-x-3">
          {isLoggedIn ? (
            <Link to="/profile">
              <img
                src={user?.profilePicture || "/userProfilePics.jpg"}
                alt="User Icon"
                className="w-10 h-10 rounded-full hover:cursor-pointer"
              />
            </Link>
          ) : (
            <>
              <Link to="/auth?mode=login">
                <button className="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded-xl transition duration-300">
                  Login
                </button>
              </Link>
              <Link to="/auth?mode=signup">
                <button className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-xl transition duration-300">
                  Sign Up
                </button>
              </Link>
            </>
          )}
        </div>

        {/* Hamburger Menu - Mobile */}
        <div className="md:hidden">
          <button
            onClick={() => setMenuOpen(!menuOpen)}
            className="text-gray-700 focus:outline-none"
            aria-label="Toggle navigation"
          >
            <svg
              className="h-6 w-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              {menuOpen ? (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M6 18L18 6M6 6l12 12"
                />
              ) : (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M4 6h16M4 12h16M4 18h16"
                />
              )}
            </svg>
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      {menuOpen && (
        <div className="md:hidden bg-white border-t border-gray-200 px-6 py-4 space-y-4">
          {/* SearchBar - Mobile */}
          <SearchBar onSearch={handleSearch} />

          {/* Auth/Profile */}
          {isLoggedIn ? (
            <div className="flex justify-center">
              <Link to="/profile" onClick={() => setMenuOpen(false)}>
                <img
                  src={user?.profilePicture || "/userProfilePics.jpg"}
                  alt="User Icon"
                  className="w-12 h-12 rounded-full hover:cursor-pointer"
                />
              </Link>
            </div>
          ) : (
            <div className="space-y-3">
              <Link to="/auth?mode=login">
                <button className="w-full bg-blue-500 hover:bg-blue-700 text-white px-3 py-2 rounded-xl transition">
                  Login
                </button>
              </Link>
              <Link to="/auth?mode=signup">
                <button className="w-full bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-xl transition">
                  Sign Up
                </button>
              </Link>
            </div>
          )}
        </div>
      )}
    </header>
  );
}

export default NavBar;

// import { Link } from "react-router-dom";
// import { useState, useEffect } from "react";
// import SearchBar from "../SearchBar";

// function NavBar() {
//   const [menuOpen, setMenuOpen] = useState(false);
//   const [isLoggedIn, setIsLoggedIn] = useState(false);
//   const [user, setUser] = useState(null);

//   useEffect(() => {
//     const userId = localStorage.getItem("user");
//     if (userId) {
//       setIsLoggedIn(true);
//       const fetchUser = async () => {
//         try {
//           const res = await fetch(`http://localhost:5000/api/users/${userId}`, {
//             method: "GET",
//             headers: {
//               "Content-Type": "application/json",
//               Authorization: `Bearer ${localStorage.getItem("token")}`,
//             },
//             credentials: "include",
//           });
//           const data = await res.json();
//           if (res.ok) {
//             setUser(data);
//           }
//         } catch (error) {
//           console.error(error);
//         }
//       };
//       fetchUser();
//     }
//   }, []);

//   return (
//     <header className="bg-white shadow-md sticky top-0 z-50">
//       <div className="max-w-7xl mx-auto flex items-center justify-between px-4 py-3 md:py-4">
//         {/* Logo */}
//         <div className="flex-shrink-0">
//           <Link to="/home" aria-label="Home">
//             <img
//               src="/campusnest_logo.png"
//               alt="CampusNest Logo"
//               className="h-12 w-12 md:h-14 md:w-14"
//             />
//           </Link>
//         </div>

//         {/* Hamburger - Mobile */}
//         <div className="md:hidden">
//           <button
//             onClick={() => setMenuOpen(!menuOpen)}
//             className="text-gray-700 focus:outline-none"
//             aria-label="Toggle navigation"
//           >
//             <svg
//               className="h-6 w-6"
//               fill="none"
//               stroke="currentColor"
//               viewBox="0 0 24 24"
//               xmlns="http://www.w3.org/2000/svg"
//             >
//               {menuOpen ? (
//                 <path
//                   strokeLinecap="round"
//                   strokeLinejoin="round"
//                   strokeWidth={2}
//                   d="M6 18L18 6M6 6l12 12"
//                 />
//               ) : (
//                 <path
//                   strokeLinecap="round"
//                   strokeLinejoin="round"
//                   strokeWidth={2}
//                   d="M4 6h16M4 12h16M4 18h16"
//                 />
//               )}
//             </svg>
//           </button>
//         </div>

//         {/* Desktop Navigation */}
//         {/* <nav className="hidden md:flex md:items-center md:space-x-6 text-gray-700">
//           <Link
//             to="/home"
//             className="hover:bg-gray-700 hover:text-white rounded-3xl px-4 py-2 transition duration-300"
//           >
//             Home
//           </Link>
//           <Link
//             to="/hostels"
//             className="hover:bg-gray-700 hover:text-white rounded-3xl px-4 py-2 transition duration-300"
//           >
//             Hostels
//           </Link>
//           <Link
//             to="/#about"
//             className="hover:bg-gray-700 hover:text-white rounded-3xl px-4 py-2 transition duration-300"
//           >
//             About
//           </Link>
//           <Link
//             to="/#contact"
//             className="hover:bg-gray-700 hover:text-white rounded-3xl px-4 py-2 transition duration-300"
//           >
//             Contact
//           </Link>
//         </nav>*/}
//         <SearchBar />

//         {/* Desktop User Section */}
//         {isLoggedIn ? (
//           <div className="hidden md:flex items-center space-x-3">
//             <Link to="/profile">
//               <img
//                 src={user?.profilePicture || "/userProfilePics.jpg"}
//                 alt="User Icon"
//                 className="w-10 h-10 rounded-full hover:cursor-pointer"
//               />
//             </Link>
//           </div>
//         ) : (
//           <div className="hidden md:flex items-center space-x-3">
//             <Link to="/auth?mode=login">
//               <button className="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded-xl transition duration-300">
//                 Login
//               </button>
//             </Link>
//             <Link to="/auth?mode=signup">
//               <button className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-xl transition duration-300">
//                 Sign Up
//               </button>
//             </Link>
//           </div>
//         )}
//       </div>

//       {/* Mobile Menu */}
//       {menuOpen && (
//         <div className="md:hidden bg-white px-6 pb-6 pt-2 text-center border-t border-gray-200">
//           {/* <ul className="space-y-3 text-gray-700 font-medium">
//             <li>
//               <Link
//                 to="/home"
//                 className="block hover:bg-gray-200 rounded-xl px-4 py-2 transition"
//                 onClick={() => setMenuOpen(false)}
//               >
//                 Home
//               </Link>
//             </li>
//             <li>
//               <Link
//                 to="/hostels"
//                 className="block hover:bg-gray-200 rounded-xl px-4 py-2 transition"
//                 onClick={() => setMenuOpen(false)}
//               >
//                 Hostels
//               </Link>
//             </li>
//             <li>
//               <Link
//                 to="/#about"
//                 className="block hover:bg-gray-200 rounded-xl px-4 py-2 transition"
//                 onClick={() => setMenuOpen(false)}
//               >
//                 About
//               </Link>
//             </li>
//             <li>
//               <Link
//                 to="/#contact"
//                 className="block hover:bg-gray-200 rounded-xl px-4 py-2 transition"
//                 onClick={() => setMenuOpen(false)}
//               >
//                 Contact
//               </Link>
//             </li>
//           </ul>*/}

//           <SearchBar />
//           {/* Mobile Auth/Profile */}
//           {isLoggedIn ? (
//             <div className="mt-6 flex justify-center">
//               <Link to="/profile" onClick={() => setMenuOpen(false)}>
//                 <img
//                   src={user?.profilePicture || "/userProfilePics.jpg"}
//                   alt="User Icon"
//                   className="w-12 h-12 rounded-full hover:cursor-pointer"
//                 />
//               </Link>
//             </div>
//           ) : (
//             <div className="mt-6 space-y-3 m-4">
//               <Link to="/auth?mode=login">
//                 <button className="w-full bg-blue-500 hover:bg-blue-700 text-white px-3 py-2 rounded-xl transition mb-2">
//                   Login
//                 </button>
//               </Link>
//               <Link to="/auth?mode=signup">
//                 <button className="w-full bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-xl transition">
//                   Sign Up
//                 </button>
//               </Link>
//             </div>
//           )}
//         </div>
//       )}
//     </header>
//   );
// }

// export default NavBar;
