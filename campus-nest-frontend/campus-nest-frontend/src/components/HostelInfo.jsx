function HostelInfo({
  name,
  address,
  description,
  availableRooms,
  totalRooms,
  manager,
}) {
  return (
    <div className="w-full bg-white rounded-xl p-8 mt-8 border border-gray-200 space-y-6">
      {/* Title & Room Count */}
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-semibold text-gray-900">{name}</h1>
        <div className="text-right">
          <p className="text-sm text-gray-500">Total Rooms</p>
          <p className="text-xl font-bold text-indigo-600">{totalRooms}</p>
        </div>
      </div>

      {/* Address */}
      <div>
        <p className="text-sm text-gray-500 mb-1">Address</p>
        <p className="text-base text-gray-700">{address}</p>
      </div>

      {/* Description */}
      {description && (
        <div>
          <p className="text-sm text-gray-500 mb-1">Description</p>
          <p className="text-base text-gray-700 leading-relaxed">
            {description}
          </p>
        </div>
      )}

      {/* Room Availability */}
      <div className="flex flex-wrap gap-4">
        <div
          className={`px-4 py-2 rounded-full text-sm font-medium border ${
            availableRooms > 0
              ? "bg-green-50 text-green-700 border-green-200"
              : "bg-red-50 text-red-700 border-red-200"
          }`}
        >
          {availableRooms > 0
            ? `Available Rooms: ${availableRooms}`
            : "No Available Rooms"}
        </div>
        <div className="px-4 py-2 rounded-full text-sm font-medium bg-blue-50 text-blue-700 border border-blue-200">
          Total Rooms: {totalRooms}
        </div>
      </div>

      {/* Manager Info */}
      <div className="pt-6 border-t border-gray-100">
        <p className="text-sm text-gray-500 mb-3">Manager</p>
        {manager ? (
          <div className="flex items-center gap-4">
            <img
              src={manager.profileImageUrl || "/userProfilePics.jpg"}
              alt="Manager profile"
              className="w-16 h-16 rounded-full object-cover"
            />
            <div className="flex-1/2">
              <p className="text-base font-semibold text-gray-800 mb-0.5">
                {manager.name}
              </p>
              <p className="text-sm text-gray-600 mb-1">
                <strong>{manager.email}</strong>
              </p>
              <p className="text-sm text-gray-600 -mx-1">
                <strong>{manager.phoneNumber}</strong>
              </p>
            </div>
          </div>
        ) : (
          <p className="text-red-500">Manager information not available</p>
        )}
      </div>
    </div>
  );
}

export default HostelInfo;
