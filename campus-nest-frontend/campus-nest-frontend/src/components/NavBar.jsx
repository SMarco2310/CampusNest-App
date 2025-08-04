import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";

function NavBar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  // useEffect(() => {
  //   const token = localStorage.getItem("token");
  //   const storedUser = localStorage.getItem("user");
  //   setIsLoggedIn(!!token);
  //   setUser(storedUser ? JSON.parse(storedUser) : null);
  // }, []);

  // const handleLogout = () => {
  //   localStorage.removeItem("token");
  //   localStorage.removeItem("user");
  //   localStorage.removeItem("locations");
  //   setIsLoggedIn(false);
  //   setUser(null);
  //   navigate("/");
  // };

  return (
    <header className="navbar-container" role="banner">
      <div className="logo-container">
        <Link to="/" aria-label="Home">
          {/* Use import or public URL for images depending on setup */}
          <img
            src="/logo.png"
            alt=" CampusNest Logo"
            width="150"
            height="200"
          />
        </Link>
      </div>

      <nav className="navbar" aria-label="Primary Navigation">
        <ul>
          <li>
            <Link to="/#home">Home</Link>
          </li>
          <li>
            <Link to="/#about">Hostels</Link>
          </li>
          <li>
            <Link to="/#about">About</Link>
          </li>
          <li>
            <Link to="/#contact">Contact</Link>
          </li>
        </ul>
      </nav>

      <div className="auth-controls">
        {isLoggedIn && user ? (
          <>
            <Link to={`/user/profile/${user.userId}`} aria-label="User Profile">
              <img
                src="/public/profile.png"
                alt={`${user.name || "User"} Profile`}
                width={36}
                height={36}
              />
            </Link>
            <button
              className="logout-btn"
              onClick={handleLogout}
              aria-label="Logout"
              type="button"
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login">
              <button className="login-btn" type="button">
                Login
              </button>
            </Link>
            <Link to="/signup">
              <button className="signup-btn" type="button">
                Sign Up
              </button>
            </Link>
          </>
        )}
      </div>
    </header>
  );
}

export default NavBar;
