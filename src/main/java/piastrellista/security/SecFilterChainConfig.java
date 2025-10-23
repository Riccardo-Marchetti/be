package piastrellista.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecFilterChainConfig {

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity, JWTFilter jwtFilter) throws Exception {
        // DISABLE DEFAULT BEHAVIORS
        // DISABLE LOGIN FORM
        httpSecurity.formLogin(http -> http.disable());
        // DISABLE CSRF PROTECTION
        httpSecurity.csrf(http -> http.disable());
        // REMOVE SESSIONS
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.cors(Customizer.withDefaults());
        // ALLOW ALL REQUESTS TO PASS THROUGH THE FILTER CHAIN, ALL RESOURCES ARE ACCESSIBLE TO ALL USERS WITH OR WITHOUT AUTHENTICATION
        httpSecurity.authorizeHttpRequests(http -> http
                // Permetti accesso pubblico agli URL del WebController
                // .requestMatchers(HttpMethod.GET, "/31.97.47.207:3000/**").permitAll()
                // Permetti accesso pubblico ai GET di /blog e /blog/{blogId}
                .requestMatchers(HttpMethod.GET, "/blog", "/blog/**").permitAll()
                // Richiedi autenticazione e ruolo ADMIN per POST, PUT, DELETE su /blog/**
                .requestMatchers(HttpMethod.POST, "/blog/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/blog/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/blog/**").hasAuthority("ADMIN")
                // Permetti accesso pubblico a /auth/**
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").denyAll()
                // Qualsiasi altra richiesta richiede autenticazione
                .anyRequest().authenticated());

        // Aggiungi il filtro JWT prima del filtro di autenticazione di default
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder (){
        // Returns a BCryptPasswordEncoder with a strength of 11
        return new BCryptPasswordEncoder(11);
    }
}
