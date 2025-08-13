import SearchBar from "../components/SearchBar";
import Header from "../components/Header";
import HostelList from "../components/HostelList";
import { useState, useEffect } from "react";

function HostelSearchPage() {
  const [hostels, setHostels] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [filteredHostels, setFilteredHostels] = useState([]);

  useEffect(() => {
    fetch("http://localhost:5000/api/hostels/hostels")
      .then((response) => response.json())
      .then((data) => setHostels(data.hostels));
  }, []);

  useEffect(() => {
    const filtered = hostels.filter((hostel) =>
      hostel.name.toLowerCase().includes(searchQuery.toLowerCase()),
    );
    setFilteredHostels(filtered);
  }, [hostels, searchQuery]);

  return (
    <div className="min-h-screen bg-gray-200 p-4">
      <SearchBar onSearch={setSearchQuery} />
      <div className="bg-gray-300 rounded-xl">
        <Header />
        <HostelList hostels={hostels} onSearch={filteredHostels} />
      </div>
    </div>
  );
}

export default HostelSearchPage;
