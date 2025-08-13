import { useState } from "react";

function HostelPictures({ pictures }) {
  const [currentIndex, setCurrentIndex] = useState(0);

  if (!pictures || pictures.length === 0) {
    return (
      <div className="w-full h-64 bg-gray-300 rounded-xl flex items-center justify-center">
        <span className="text-gray-700">No pictures available</span>
      </div>
    );
  }

  const goPrev = () => {
    setCurrentIndex((prev) => (prev === 0 ? pictures.length - 1 : prev - 1));
  };

  const goNext = () => {
    setCurrentIndex((prev) => (prev === pictures.length - 1 ? 0 : prev + 1));
  };

  return (
    <div className="relative w-full h-64 rounded-xl overflow-hidden">
      <img
        src={pictures[currentIndex]}
        alt={`Hostel pic ${currentIndex + 1}`}
        className="w-full h-full object-cover rounded-xl"
      />
      <button
        onClick={goPrev}
        className="absolute left-2 top-1/2 transform -translate-y-1/2 bg-white/70 hover:bg-white px-3 py-1 rounded-full"
      >
        ‹
      </button>
      <button
        onClick={goNext}
        className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-white/70 hover:bg-white px-3 py-1 rounded-full"
      >
        ›
      </button>
    </div>
  );
}

export default HostelPictures;
