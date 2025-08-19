import React, { useState } from 'react';
import { Filter, ChevronDown } from 'lucide-react';
import { FilterOptions } from '../../types/interfaces';

interface FilterBarProps {
  filters: FilterOptions;
  onFilterChange: (filters: FilterOptions) => void;
}

const FilterBar: React.FC<FilterBarProps> = ({ filters, onFilterChange }) => {
  const [isOpen, setIsOpen] = useState(false);

  const amenitiesList = ['AC','Television', 'Kitchen', 'Laundry', 'Gym', 'Study Room', 'Parking'];
  const roomTypes = ['single', 'double', '3-beds', '4-beds'];

  const handlePriceChange = (min: number, max: number) => {
    onFilterChange({
      ...filters,
      priceRange: [min, max]
    });
  };

  const handleRoomTypeChange = (type: string, checked: boolean) => {
    const newTypes = checked
      ? [...filters.roomType, type]
      : filters.roomType.filter(t => t !== type);
    
    onFilterChange({
      ...filters,
      roomType: newTypes
    });
  };

  const handleAmenityChange = (amenity: string, checked: boolean) => {
    const newAmenities = checked
      ? [...filters.amenities, amenity]
      : filters.amenities.filter(a => a !== amenity);
    
    onFilterChange({
      ...filters,
      amenities: newAmenities
    });
  };

  return (
    <div className="bg-white shadow-sm border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div className="flex items-center justify-between">
          <button
            onClick={() => setIsOpen(!isOpen)}
            className="flex items-center space-x-2 px-4 py-2 bg-blue-50 text-blue-600 rounded-lg hover:bg-blue-100 transition-colors"
          >
            <Filter className="h-5 w-5" />
            <span>Filters</span>
            <ChevronDown className={`h-4 w-4 transform transition-transform ${isOpen ? 'rotate-180' : ''}`} />
          </button>
          
          <div className="flex items-center space-x-4 text-sm text-gray-600">
            <span>{filters.roomType.length > 0 ? `${filters.roomType.join(', ')} rooms` : 'All Hostels'}</span>
            <span>₵{filters.priceRange[0]} - ₵{filters.priceRange[1]}</span>
            <span>Within {filters.maxDistance}km</span>
          </div>
        </div>

        {isOpen && (
          <div className="mt-4 grid grid-cols-1 md:grid-cols-4 gap-6 p-6 bg-gray-50 rounded-lg animate-in slide-in-from-top-2 duration-200">
            {/* Price Range */}
            <div>
              <h3 className="font-medium text-gray-900 mb-3">Price Range</h3>
              <div className="space-y-2">
                <input
                  type="range"
                  min="4000"
                  max="10000"
                  value={filters.priceRange[0]}
                  onChange={(e) => handlePriceChange(parseInt(e.target.value), filters.priceRange[1])}
                  className="w-full accent-blue-600"
                />
                <input
                  type="range"
                  min="4000"
                  max="10000"
                  value={filters.priceRange[1]}
                  onChange={(e) => handlePriceChange(filters.priceRange[0], parseInt(e.target.value))}
                  className="w-full accent-blue-600"
                />
                <div className="flex justify-between text-sm text-gray-600">
                  <span>₵{filters.priceRange[0]}</span>
                  <span>₵{filters.priceRange[1]}</span>
                </div>
              </div>
            </div>

            {/* Room Type */}
            <div>
              <h3 className="font-medium text-gray-900 mb-3">Room Type</h3>
              <div className="space-y-2">
                {roomTypes.map(type => (
                  <label key={type} className="flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      checked={filters.roomType.includes(type)}
                      onChange={(e) => handleRoomTypeChange(type, e.target.checked)}
                      className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                    />
                    <span className="ml-2 text-sm capitalize">{type}</span>
                  </label>
                ))}
              </div>
            </div>

            {/* Amenities */}
            <div>
              <h3 className="font-medium text-gray-900 mb-3">Amenities</h3>
              <div className="space-y-2 max-h-32 overflow-y-auto">
                {amenitiesList.map(amenity => (
                  <label key={amenity} className="flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      checked={filters.amenities.includes(amenity)}
                      onChange={(e) => handleAmenityChange(amenity, e.target.checked)}
                      className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                    />
                    <span className="ml-2 text-sm">{amenity}</span>
                  </label>
                ))}
              </div>
            </div>

            {/* Distance */}
            <div>
              <h3 className="font-medium text-gray-900 mb-3">Max Distance</h3>
              <div className="space-y-2">
                <input
                  type="range"
                  min="0.5"
                  max="10"
                  step="0.5"
                  value={filters.maxDistance}
                  onChange={(e) => onFilterChange({ ...filters, maxDistance: parseFloat(e.target.value) })}
                  className="w-full accent-blue-600"
                />
                <div className="text-sm text-gray-600">{filters.maxDistance}km from campus</div>
              </div>
              <label className="flex items-center mt-3 cursor-pointer">
                <input
                  type="checkbox"
                  checked={filters.availableOnly}
                  onChange={(e) => onFilterChange({ ...filters, availableOnly: e.target.checked })}
                  className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                />
                <span className="ml-2 text-sm">Available only</span>
              </label>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default FilterBar;