package com.gestion.backend.services;

import com.gestion.backend.dtos.auth.LoginRequest;
import com.gestion.backend.dtos.auth.LoginResponse;
import com.gestion.backend.dtos.auth.RegisterRequest;
import com.gestion.backend.dtos.auth.RegisterResponse;
import com.gestion.backend.entities.AppUser;
import com.gestion.backend.entities.Etudiant;
import com.gestion.backend.entities.Role;
import com.gestion.backend.repositories.AppUserRepository;
import com.gestion.backend.repositories.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final EtudiantRepository etudiantRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (appUserRepository.findByUsername(request.getEmail()).isPresent()) {
            return RegisterResponse.builder()
                    .success(false)
                    .message("Email déjà utilisé.")
                    .build();
        }

        // Create AppUser
        AppUser user = AppUser.builder()
                .username(request.getEmail()) // Using email as username
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .role(Role.ETUDIANT)
                .build();
        appUserRepository.save(user);

        // Create Etudiant
        // Generating a matricule (e.g., "ETUD" + random or timestamp, simply using
        // email hash or time for simplicity)
        String matricule = "ETUD" + System.currentTimeMillis();
        Etudiant etudiant = Etudiant.builder()
                .matricule(matricule)
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .dateInscription(LocalDate.now())
                .build();
        etudiantRepository.save(etudiant);

        return RegisterResponse.builder()
                .success(true)
                .message("Inscription réussie.")
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUser user = (AppUser) authentication.getPrincipal();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        return LoginResponse.builder()
                .token(sessionId) // Returning Session ID as token context
                .role(user.getRole().name())
                .message("Connexion réussie.")
                .build();
    }

    public boolean checkEmail(String email) {
        return appUserRepository.findByUsername(email).isPresent();
    }
}
