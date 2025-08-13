import RoomItem from "./RoomItem";

function RoomsList({ rooms }) {
  return (
    <div className="mt-8">
      <h1 className="text-gray-700 font-bold mb-4 inline-block px-2 py-2 text-2xl">
        Rooms
      </h1>
      <div className="space-y-4">
        {rooms.map((room) => (
          <RoomItem
            key={room.id}
            roomNumber={room.roomNumber}
            roomCapacity={room.roomCapacity}
            currentOccupancy={room.currentOccupancy}
            pricePerBed={room.pricePerBed}
            description={room.description}
            picture={room.imageUrls}
          />
        ))}
      </div>
    </div>
  );
}

export default RoomsList;
