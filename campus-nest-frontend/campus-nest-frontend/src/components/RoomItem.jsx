function RoomItem({
  roomNumber,
  roomCapacity,
  currentOccupancy,
  pricePerBed,
  description,
  imageUrls = [],
}) {
  const picture = imageUrls[0];

  return (
    <div className="border rounded-lg shadow-sm p-4 mb-4 bg-white hover:shadow-md transition">
      {picture && (
        <div className="w-full h-40 mb-3 rounded-md overflow-hidden bg-gray-200">
          <img
            src={picture}
            alt={`Room ${roomNumber}`}
            className="w-full h-full object-cover"
          />
        </div>
      )}

      <div className="flex justify-between items-center mb-2">
        <h3 className="text-lg font-semibold text-gray-800">
          Room {roomNumber}
        </h3>
        <span className="bg-gray-200 text-gray-800 text-xs font-medium px-3 py-1 rounded-full">
          {roomCapacity}
        </span>
      </div>

      <p className="text-sm text-gray-600 mb-2">{description}</p>

      <div className="flex justify-between items-center text-sm">
        <div className="text-blue-700 font-medium">
          GH₵ {pricePerBed.toLocaleString()}
          <span className="text-gray-500"> / bed</span>
        </div>
        <div className="text-gray-600">Occupancy: {currentOccupancy}</div>
      </div>
    </div>
  );
}

export default RoomItem;
