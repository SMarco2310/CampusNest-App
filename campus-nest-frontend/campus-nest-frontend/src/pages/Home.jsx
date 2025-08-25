import { useState } from "react";
import NavBar from "../components/NavBar";
import FilterBar from "../components/Filterbar";

export default function Home() {
  const [filters, setFilters] = useState({
    priceRange: [0, 1000],
    roomType: [],
    amenities: [],
    maxDistance: 10,
    availableOnly: false,
  });

  return (
    <>
      <NavBar />
      <FilterBar filters={filters} onFilterChange={setFilters} />
      
    </>
  );
}