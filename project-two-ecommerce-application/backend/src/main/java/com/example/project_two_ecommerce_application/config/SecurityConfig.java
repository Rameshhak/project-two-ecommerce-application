package com.example.project_two_ecommerce_application.config;

import com.example.project_two_ecommerce_application.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Allow session creation when needed (for guest carts)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 🟢 Public endpoints
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/user/register",
                                "/api/products/display-all-products",
                                "/api/products",
                                //"/api/cart/**",// Guest cart access
                                "/v3/api-docs/**",
                                "/swagger-ui/**"
                        ).permitAll()
                        // 🔒 Admin endpoints
                        .requestMatchers(
                                "/api/products/create",
                                "/api/products/update/**",
                                "/api/products/delete/**"
                        ).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority("ROLE_ADMIN")

                        // 🧑‍💼 Customer & Admin (JWT required)
                        .requestMatchers("/api/cart/**", "/api/orders/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")

                        // 🚫 Everything else requires authentication
                        .requestMatchers(HttpMethod.GET, "/api/user/get-user/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/user/update/{id}").authenticated()
                        .anyRequest().authenticated()
                )
                // Add JWT filter (but we’ll make it skip public paths)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Match the settings from your previous CorsConfig
        configuration.setAllowedOrigins(List.of("https://singular-cajeta-a14474.netlify.app/"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // allow the common headers and the Authorization header
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        // expose Authorization header if you send it back (not always required)
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // apply global CORS to everything
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}


