package br.com.youx.clinica.security;

import br.com.youx.clinica.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AuthenticationEntryPoint personalizado para tratar erros de autenticação JWT
 * Intercepta erros antes que cheguem aos controllers
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        
        log.warn("Erro de autenticação na requisição: {} - {}", 
                request.getRequestURI(), authException.getMessage());

        // Verifica se há informações específicas sobre o tipo de erro JWT
        String jwtError = (String) request.getAttribute("jwt.error");
        String jwtErrorMessage = (String) request.getAttribute("jwt.error.message");

        ErrorResponseDTO errorResponse;
        
        if ("TOKEN_EXPIRED".equals(jwtError)) {
            errorResponse = ErrorResponseDTO.of(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Token Expirado", 
                    "Seu token de acesso expirou. Faça login novamente.",
                    request.getRequestURI()
            );
        } else if ("TOKEN_INVALID".equals(jwtError)) {
            errorResponse = ErrorResponseDTO.of(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Token Inválido",
                    "Token de acesso inválido. Faça login novamente.", 
                    request.getRequestURI()
            );
        } else if ("TOKEN_MISSING".equals(jwtError)) {
            errorResponse = ErrorResponseDTO.of(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Token Ausente",
                    "Token de acesso é obrigatório. Faça login para acessar este recurso.",
                    request.getRequestURI()
            );
        } else {
            // Erro genérico de autenticação
            errorResponse = ErrorResponseDTO.of(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Erro de Autenticação",
                    jwtErrorMessage != null ? jwtErrorMessage : "Acesso não autorizado. Faça login para continuar.",
                    request.getRequestURI()
            );
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // Adiciona headers CORS para evitar problemas no frontend
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
} 