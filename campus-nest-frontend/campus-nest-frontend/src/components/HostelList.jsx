import HostelCard from "./HostelCard";

function HostelList({ hostels }) {
  if (hostels.length === 0) {
    return (
      <div className="p-4 text-center text-gray-500">No hostels found.</div>
    );
  }

  return (
    <div className="p-4 space-y-4">
      {hostels.map((hostel, index) => (
        <HostelCard key={index} hostel={hostel} />
      ))}
    </div>
  );
}

export default HostelList;
