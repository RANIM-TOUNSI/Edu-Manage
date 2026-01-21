package com.gestion.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/login", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/formateur/**").hasRole("TRAINER")

                        // STUDENT/TRAINER/ADMIN: View student profiles
                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("ADMIN", "TRAINER", "STUDENT")
                        .requestMatchers("/api/students/**").hasRole("ADMIN")
                        .requestMatchers("/api/trainers/**").hasRole("ADMIN")

                        // STUDENT: View own grades, enrollments, reports, and schedule
                        .requestMatchers(HttpMethod.GET, "/api/grades/student/**")
                        .hasAnyRole("ADMIN", "STUDENT", "TRAINER")
                        .requestMatchers(HttpMethod.GET, "/api/enrollments/student/**")
                        .hasAnyRole("ADMIN", "STUDENT", "TRAINER")
                        .requestMatchers(HttpMethod.GET, "/api/reports/student/**")
                        .hasAnyRole("ADMIN", "STUDENT", "TRAINER")
                        .requestMatchers(HttpMethod.GET, "/api/planning/my-schedule").hasAnyRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/planning/trainer/**").hasAnyRole("TRAINER", "ADMIN")

                        // TRAINER/ADMIN: Manage grades, enrollments, reports
                        .requestMatchers("/api/grades/**").hasAnyRole("ADMIN", "TRAINER")
                        .requestMatchers("/api/enrollments/**").hasAnyRole("ADMIN", "TRAINER")
                        .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "TRAINER")
                        .requestMatchers("/api/planning/**").hasAnyRole("ADMIN")

                        // COURSES: View courses
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").hasAnyRole("ADMIN", "TRAINER", "STUDENT")
                        .requestMatchers("/api/courses/**").hasAnyRole("ADMIN", "TRAINER")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            var authorities = authentication.getAuthorities();
                            String targetUrl = "/";
                            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                targetUrl = "/admin/etudiants";
                            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_TRAINER"))) {
                                targetUrl = "/formateur/dashboard";
                            }
                            response.sendRedirect(targetUrl);
                        })
                        .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login").permitAll())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of("http://localhost:4200"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
