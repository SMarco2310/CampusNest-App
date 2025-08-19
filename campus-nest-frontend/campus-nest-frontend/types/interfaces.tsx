export interface Room {
  id: string;
  name: string;
  hostelName: string;
  price: number;
  type: 'single' | 'double' | 'shared';
  amenities: string[];
  distanceFromCampus: number;
  available: boolean;
  image: string;
  description: string;
  location: string;
  capacity: number;
  currentOccupancy: number;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'student' | 'admin';
  preferences?: RoommatePreferences;
}

export interface RoommatePreferences {
  gender: 'male' | 'female' | 'any';
  lifestyle: 'quiet' | 'social' | 'mixed';
  cleanliness: 'very-clean' | 'clean' | 'relaxed';
  studyHabits: 'early-bird' | 'night-owl' | 'flexible';
}

export interface Booking {
  id: string;
  userId: string;
  roomId: string;
  status: 'pending' | 'confirmed' | 'cancelled';
  checkInDate: string;
  duration: number;
  totalAmount: number;
  createdAt: string;
}

export interface Complaint {
  id: string;
  userId: string;
  roomId: string;
  type: 'maintenance' | 'noise' | 'cleanliness' | 'landlord' | 'other';
  title: string;
  description: string;
  status: 'open' | 'in-progress' | 'resolved';
  createdAt: string;
}

export interface FilterOptions {
  priceRange: [number, number];
  roomType: string[];
  amenities: string[];
  maxDistance: number;
  availableOnly: boolean;
}