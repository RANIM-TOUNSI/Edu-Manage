package com.gestion.backend.controllers.api;

import com.gestion.backend.dtos.LoginRequest;
import com.gestion.backend.dtos.LoginResponse;
import com.gestion.backend.dtos.RegisterRequest;
import com.gestion.backend.entities.Student;
import com.gestion.backend.entities.Trainer;
import com.gestion.backend.entities.User;
import com.gestion.backend.repositories.StudentRepository;
import com.gestion.backend.repositories.TrainerRepository;
import com.gestion.backend.repositories.UserRepository;
import com.gestion.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TrainerRepository trainerRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        Long trainerId = user.getTrainerId();
        Long studentId = user.getStudentId();

        // Auto-link student if ID is null but role is STUDENT
        if (studentId == null && user.getRole() == User.Role.STUDENT) {
            System.out.println("DEBUG: Linking student for user " + user.getUsername());
            studentId = studentRepository.findByEmail(user.getUsername())
                    .map(Student::getId)
                    .orElse(null);

            if (studentId == null) {
                studentId = studentRepository.findByMatricule(user.getUsername())
                        .map(Student::getId)
                        .orElse(null);
            }

            if (studentId != null) {
                user.setStudentId(studentId);
                userRepository.save(user);
                System.out.println("DEBUG: Successfully linked student ID " + studentId);
            } else {
                System.out.println(
                        "DEBUG: Could not find student record for matching username/email: " + user.getUsername());
            }
        }

        // Auto-link trainer if ID is null but role is TRAINER
        if (trainerId == null && user.getRole() == User.Role.TRAINER) {
            System.out.println("DEBUG: Linking trainer for user " + user.getUsername());
            trainerId = trainerRepository.findByEmail(user.getUsername())
                    .map(Trainer::getId)
                    .orElse(null);

            if (trainerId == null) {
                trainerId = trainerRepository.findByNameContainingIgnoreCase(user.getUsername())
                        .stream().map(Trainer::getId).findFirst().orElse(null);
            }

            if (trainerId != null) {
                user.setTrainerId(trainerId);
                userRepository.save(user);
                System.out.println("DEBUG: Successfully linked trainer ID " + trainerId);
            }
        }

        LoginResponse.UserDto userDto = new LoginResponse.UserDto(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                trainerId,
                studentId);

        return ResponseEntity.ok(new LoginResponse(jwt, userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Username already exists"));
        }

        User.Role role;
        try {
            role = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Invalid role: " + request.getRole()));
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        if (role == User.Role.STUDENT) {
            // Check for existing student with same email
            Student student = studentRepository.findByEmail(request.getEmail())
                    .orElseGet(() -> studentRepository.save(Student.builder()
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .email(request.getEmail())
                            .matricule("STU-" + System.currentTimeMillis())
                            .registrationDate(LocalDate.now())
                            .build()));
            user.setStudentId(student.getId());
        } else if (role == User.Role.TRAINER) {
            // Check for existing trainer with same email
            Trainer trainer = trainerRepository.findByEmail(request.getEmail())
                    .orElseGet(() -> trainerRepository.save(Trainer.builder()
                            .name(request.getFirstName() + " " + request.getLastName())
                            .email(request.getEmail())
                            .specialty("General")
                            .build()));
            user.setTrainerId(trainer.getId());
        }

        userRepository.save(user);

        return ResponseEntity.ok(java.util.Map.of("message", "User registered successfully"));
    }
}
