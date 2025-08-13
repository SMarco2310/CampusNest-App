import { Link } from "react-router-dom";

function HostelCard(props) {
  const { hostel } = props;

  const image =
    hostel?.imageUrls.length > 0 ? hostel.imageUrls[0] : "/placeholder.png"; // fallback image

  return (
    <Link
      to={`/hostel/${hostel?.id}`}
      className="block bg-white rounded-xl shadow-sm hover:shadow-md transition-shadow duration-200 overflow-hidden mb-3"
    >
      <div className="flex items-start p-5 gap-4">
        {/* Image */}
        <img
          src={image}
          alt={hostel?.name}
          className="w-30 h-30 object-cover rounded-md bg-gray-200 flex-shrink-0"
        />

        {/* Info */}
        <div className="flex-1 space-y-0.5">
          <h2 className="text-sm font-semibold text-gray-800 truncate">
            {hostel?.name}
          </h2>
          <p className="text-xs text-gray-500 truncate">{hostel?.address}</p>
          <p className="text-xs text-gray-400 line-clamp-1">
            {hostel?.description}
          </p>

          <div className="flex justify-between text-xs text-gray-500 mt-1">
            <span>
              Available:{" "}
              <span className="text-green-600">{hostel?.availableRooms}</span>
            </span>
            <span>
              {hostel?.availableRooms}/{hostel?.totalRooms}
            </span>
          </div>

          {/* More info link (not redundant since it's small) */}
          <div>
            <span className="text-blue-500 hover:underline text-xs">
              More info
            </span>
          </div>
        </div>
      </div>
    </Link>
  );
}

export default HostelCard;
