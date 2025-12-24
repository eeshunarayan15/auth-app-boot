package com.eeshu.auth.securityConfig;

import com.eeshu.auth.security.JwtsAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtsAuthenticationFilter jwtsAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(

                        authorizeHttpRequests ->
                                authorizeHttpRequests
                                        .requestMatchers("/api/v1/auth/signin").permitAll()
                                        .requestMatchers("/api/v1/auth/signup").permitAll()
                                        .anyRequest().authenticated()


                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(401, "Unauthorized");
                    // Log the exception for debugging purposes
                    authException.printStackTrace();
                    response.setContentType("application/json");
                    String message = "Unauthorised access" + authException.getMessage();
                    Map<String, String> errorMap = Map.of("message", message, "status", String.valueOf(401), "statusCode", Integer.toString(401));
                    var objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(errorMap));
                }))
                .addFilterBefore(jwtsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
