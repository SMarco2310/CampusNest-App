import HostelPictures from "../components/HostelPictures";
import HostelInfo from "../components/HostelInfo";
import RoomsList from "../components/RoomsList";
import { useEffect, useState } from "react";

function HostelDetails({ hostelId }) {
  const [hostel, setHostel] = useState(null);
  const [loading, setLoading] = useState(true);
  const [rooms, setRooms] = useState([]);

  console.log(hostel);
  useEffect(() => {
    fetch(`http://localhost:5000/api/hostels/details/${hostelId}`)
      .then((response) => response.json())
      .then((data) => {
        setHostel(data.hostel);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Failed to fetch hostel details:", error);
        setLoading(false);
      });
  }, [hostelId]);

  useEffect(() => {
    if (hostelId) {
      fetch(`http://localhost:5000/api/rooms/rooms/${hostelId}`)
        .then((response) => response.json())
        .then((data) => {
          setRooms(data.rooms);
        })
        .catch((error) => {
          console.error("Failed to fetch rooms:", error);
        });
    }
  }, [hostelId]);

  if (loading) {
    return <div className="text-center py-10 text-gray-600">Loading...</div>;
  }

  if (!hostel) {
    return (
      <div className="text-center py-10 text-red-500">Hostel not found.</div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto p-6 space-y-6">
      <HostelPictures pictures={hostel.imageUrls || []} />
      <HostelInfo
        name={hostel.name}
        address={hostel.address}
        description={hostel.description}
        availableRooms={hostel.availableRooms}
        totalRooms={hostel.totalRooms}
        manager={hostel.manager}
      />
      <RoomsList rooms={rooms || []} />
      {/* <ReviewsList reviews={hostel.reviews || []} />*/}
    </div>
  );
}

export default HostelDetails;
