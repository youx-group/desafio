package br.com.youx.clinica.security;

import br.com.youx.clinica.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de autenticação JWT
 * Intercepta todas as requisições para validar tokens JWT
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Pula a validação para endpoints de autenticação
        if (isAuthEndpoint(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt = jwtService.extractTokenFromHeader(authHeader);
        
        // Se não há token ou usuário já está autenticado, continua
        if (jwt == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // Valida o token
            if (jwtService.isTokenValid(jwt) && !jwtService.isTokenExpired(jwt)) {
                // Extrai informações do token
                String cpf = jwtService.extractCpf(jwt);
                String role = jwtService.extractRole(jwt);
                Long userId = jwtService.extractUserId(jwt);
                String nome = jwtService.extractNome(jwt);
                
                // Cria as authorities baseadas na role
                List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
                );
                
                // Cria o token de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    cpf, // Principal (CPF do usuário)
                    null, // Credentials (null pois já foi validado)
                    authorities
                );
                
                // Adiciona detalhes da requisição
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Adiciona informações adicionais no contexto
                JwtAuthenticationToken jwtAuthToken = new JwtAuthenticationToken(
                    cpf, authorities, userId, nome, role
                );
                jwtAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Define no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(jwtAuthToken);
            }
        } catch (Exception e) {
            // Token inválido - remove qualquer autenticação existente
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Verifica se o endpoint é de autenticação e não precisa de token
     * @param uri URI da requisição
     * @return true se é endpoint de auth, false caso contrário
     */
    private boolean isAuthEndpoint(String uri) {
        return uri.equals("/api/usuarios/login") ||
               uri.equals("/api/usuarios/validar-credenciais") ||
               uri.equals("/api/usuarios/registrar") ||  // Permite registro público
               uri.equals("/h2-console") ||
               uri.startsWith("/h2-console/");
    }
} 