package br.com.youx.clinica.config;

import br.com.youx.clinica.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Configuração de segurança da aplicação
 * Define o PasswordEncoder com BCrypt e configura autenticação JWT
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final SecurityProperties securityProperties;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    
    /**
     * Configura o PasswordEncoder com BCrypt usando salt personalizado
     * @return PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityProperties.getBcryptStrength());
    }
    
    /**
     * Configuração de segurança HTTP com autenticação JWT
     * Define endpoints públicos e protegidos
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints específicos PÚBLICOS (SEMPRE PRIMEIRO - ordem importa!)
                .requestMatchers("/api/usuarios/login").permitAll()
                .requestMatchers("/api/usuarios/validar-credenciais").permitAll()
                .requestMatchers("/api/usuarios/registrar").permitAll()
                // Endpoints específicos PROTEGIDOS
                .requestMatchers("/api/usuarios/me").authenticated()
                .requestMatchers("/api/usuarios/refresh-token").authenticated()
                .requestMatchers("/api/usuarios/validate-token").authenticated()
                .requestMatchers("/api/usuarios/{id}/alterar-senha").authenticated()
                // Demais endpoints de usuários PROTEGIDOS
                .requestMatchers("/api/usuarios/**").authenticated()
                // Outros recursos protegidos
                .requestMatchers("/api/pacientes/**").authenticated() 
                .requestMatchers("/api/consultas/**").authenticated()
                // Qualquer outra requisição precisa estar autenticada
                .anyRequest().authenticated()
            )
            // Adiciona o filtro JWT antes do filtro de autenticação padrão
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
} 