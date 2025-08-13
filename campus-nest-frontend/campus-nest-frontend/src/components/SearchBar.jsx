import React, { useState } from "react";

function SearchBar({ onSearch }) {
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <div className="flex justify-center my-4">
      <div className="bg-gray-300 px-4 py-2 rounded-full flex items-center w-3/4 max-w-2xl">
        <input
          type="text"
          placeholder="search your desired hostel here"
          className="bg-transparent outline-none w-full px-2"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button
          className="bg-gray-300 text-black px-2 py-2 rounded-full ml-1"
          onClick={(e) => {
            e.key === "Enter" && onSearch(searchTerm);
          }}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={1.5}
            stroke="currentColor"
            className="w-5 h-5"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 104.5 4.5a7.5 7.5 0 0012.15 12.15z"
            />
          </svg>
        </button>
      </div>
    </div>
  );
}

export default SearchBar;
