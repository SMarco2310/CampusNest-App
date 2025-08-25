package com.example.campus_nest_backend.service;

import com.example.campus_nest_backend.dto.Requests.*;
import com.example.campus_nest_backend.dto.Responses.UserResponseDto;
import com.example.campus_nest_backend.entity.*;
import com.example.campus_nest_backend.exception.DuplicateEmailException;
import com.example.campus_nest_backend.exception.UserNotFoundException;
import com.example.campus_nest_backend.repository.HostelRepository;
import com.example.campus_nest_backend.repository.RoomRepository;
import com.example.campus_nest_backend.repository.UserRepository;
import com.example.campus_nest_backend.utils.Role;
import com.example.campus_nest_backend.utils.utils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final HostelRepository hostelRepository;

    // ===== GET ALL USERS =====
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // ===== GET ALL USERS BY ROLE =====
    public List<UserResponseDto> getAllStudents() {
        return userRepository.findAllByRole(Role.STUDENT)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public List<UserResponseDto> getAllManagers() {
        return userRepository.findAllByRole(Role.HOSTEL_MANAGER)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }
    public List<UserResponseDto> getAllAdmins() {
        return userRepository.findAllByRole(Role.ADMIN)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // ===== CREATE USER =====
    @Transactional
    public User createUser(UserRegistrationRequestDto request) {
        validateRegistrationRequest(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }

        User user;
        Role role = request.getRole() != null ? request.getRole() : Role.STUDENT;

        switch (role) {
            case STUDENT -> {
                Student student = new Student();
                student.setStudentId(request.getStudentId());
                student.setCourse(request.getCourse());
                student.setClassYear(request.getClassYear());

                if (request.getCurrentRoomId() != null) {
                    Room room = roomRepository.findById(request.getCurrentRoomId())
                            .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + request.getCurrentRoomId()));
                    student.setCurrentRoom(room);
                }

                user = student;
            }

            case HOSTEL_MANAGER -> {
                Hostel_Manager manager = new Hostel_Manager();

                if (request.getBankAccountDetails() != null && !request.getBankAccountDetails().isEmpty()) {
                    List<BankAccountDetails> accounts = request.getBankAccountDetails()
                            .stream()
                            .map(this::toEntity)
                            .peek(acc -> acc.setManager(manager))   // 🔥 set back-reference
                            .toList();

                    manager.setBankAccountDetails(accounts);
//                    manager.getOwnedHostels().forEach(hostel -> hostel.setBankAccountDetails(accounts));
                }

                user = manager;
            }

            case ADMIN -> user = new Admin();

            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        }

        // ===== COMMON FIELDS =====
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(utils.passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setRole(role);
        user.setProfilePicture(request.getProfilePicture());

        return userRepository.save(user);
    }


    // ===== UPDATE USER =====

    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto request) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        // ===== COMMON UPDATES =====
        if (StringUtils.hasText(request.getName())) existingUser.setName(request.getName());
        if (StringUtils.hasText(request.getPhone())) existingUser.setPhone(request.getPhone());
        if (request.getProfilePicture() != null) existingUser.setProfilePicture(request.getProfilePicture());

        // ===== TYPE-SPECIFIC UPDATES =====
        if (existingUser instanceof Student student) {
            if (StringUtils.hasText(request.getCourse())) student.setCourse(request.getCourse());
            student.setStudentId(request.getStudentId());
            student.setClassYear(request.getClassYear());
        }

        // Admin has no extra fields for now, but you can add here if needed
        return mapToUserResponse(userRepository.save(existingUser));
    }


    // ===== UPDATE PASSWORD =====
    @Transactional
    public void updatePassword(Long id, PasswordUpdateRequestDto request) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        if (!utils.passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (request.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long");
        }

        user.setPassword(utils.passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void updateBankDetails(Long userId, List<BankAccountDetailsRequestDto> bankDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (!(user instanceof Hostel_Manager manager)) {
            throw new IllegalArgumentException("User is not a Hostel Manager");
        }

        List<BankAccountDetails> accounts = bankDetails.stream()
                .map(this::toEntity)
                .peek(acc -> acc.setManager(manager)) // set back-reference
                .collect(Collectors.toList());


//         ✅ Mutate collection instead of replacing
        manager.getBankAccountDetails().clear();
        manager.getBankAccountDetails().addAll(accounts);
//        manager.getOwnedHostels().forEach(hostel -> hostel.setBankAccountDetails(accounts));


        userRepository.save(manager);
    }


    // ===== DELETE USER =====
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");
        if (!userRepository.existsById(id)) throw new UserNotFoundException("User not found with ID: " + id);

        userRepository.deleteById(id);
    }

    // ===== GET USER BY ID =====
    public UserResponseDto getUserById(Long id) {
        if (id == null) throw new IllegalArgumentException("User ID cannot be null");

        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    // ===== LOGIN =====
    public User authenticateUser(UserLoginRequestDto request) {
        validateLoginRequest(request);

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !utils.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid email or password");
        }

        return user;
    }

    // ===== SECURITY LOAD USER =====
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!StringUtils.hasText(email)) throw new IllegalArgumentException("Email cannot be empty");

        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found with email: " + email);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    // ===== MAPPING =====
    private UserResponseDto mapToUserResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getGender(),
                user.getRole(),
                user.getProfilePicture(),
                user.getDateJoined()
        );
    }
    private BankAccountDetails toEntity(BankAccountDetailsRequestDto dto) {
        if (dto == null) {
            return null;
        }


        BankAccountDetails entity = new BankAccountDetails();
        entity.setAccountName(dto.getAccountName());
        entity.setBankName(dto.getBankName());
        entity.setBankCode(dto.getBankCode());
        entity.setCurrency(dto.getCurrency());
        entity.setAccountNumber(dto.getAccountNumber());
        // ⚠️ Manager and Hostel will be set separately in the Service layer
        if (dto.getManagerId() != null) {
            Hostel_Manager manager = (Hostel_Manager) userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            entity.setManager(manager);
        } else if (dto.getHostelId() != null) {
            Hostel hostel = hostelRepository.findById(dto.getHostelId())
                    .orElseThrow(() -> new RuntimeException("Hostel not found"));
            entity.setHostel(hostel);
        }
        return entity;
    }

    // ===== VALIDATION =====
    private void validateRegistrationRequest(UserRegistrationRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Registration request cannot be null");
        if (!StringUtils.hasText(request.getName())) throw new IllegalArgumentException("Name cannot be empty");
        if (!StringUtils.hasText(request.getEmail()) || isValidEmail(request.getEmail()))
            throw new IllegalArgumentException("Invalid email format");
        if (!StringUtils.hasText(request.getPassword()) || request.getPassword().length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters long");
    }

    private void validateLoginRequest(UserLoginRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Login request cannot be null");
        if (!StringUtils.hasText(request.getEmail())) throw new IllegalArgumentException("Email cannot be empty");
        if (!StringUtils.hasText(request.getPassword())) throw new IllegalArgumentException("Password cannot be empty");
    }

    private boolean isValidEmail(String email) {
        return email == null || !email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
}
