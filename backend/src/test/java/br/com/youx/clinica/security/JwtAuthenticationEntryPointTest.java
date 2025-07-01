package br.com.youx.clinica.security;

import br.com.youx.clinica.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para JwtAuthenticationEntryPoint
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @BeforeEach
    void setUp() throws IOException {
        when(request.getRequestURI()).thenReturn("/api/test");
        when(response.getWriter()).thenReturn(printWriter);
        when(objectMapper.writeValueAsString(any(ErrorResponseDTO.class))).thenReturn("{\"error\":\"test\"}");
    }

    @Test
    void commence_DeveRetornarTokenExpirado_QuandoJwtErrorTokenExpired() throws IOException {
        // Arrange
        when(request.getAttribute("jwt.error")).thenReturn("TOKEN_EXPIRED");
        when(request.getAttribute("jwt.error.message")).thenReturn("Token expirado");

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setHeader("Access-Control-Allow-Origin", "*");
        verify(response).setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        verify(response).setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        verify(printWriter).write("{\"error\":\"test\"}");
        verify(objectMapper).writeValueAsString(any(ErrorResponseDTO.class));
    }

    @Test
    void commence_DeveRetornarTokenInvalido_QuandoJwtErrorTokenInvalid() throws IOException {
        // Arrange
        when(request.getAttribute("jwt.error")).thenReturn("TOKEN_INVALID");
        when(request.getAttribute("jwt.error.message")).thenReturn("Token inválido");

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(objectMapper).writeValueAsString(any(ErrorResponseDTO.class));
    }

    @Test
    void commence_DeveRetornarTokenAusente_QuandoJwtErrorTokenMissing() throws IOException {
        // Arrange
        when(request.getAttribute("jwt.error")).thenReturn("TOKEN_MISSING");
        when(request.getAttribute("jwt.error.message")).thenReturn("Token ausente");

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(objectMapper).writeValueAsString(any(ErrorResponseDTO.class));
    }

    @Test
    void commence_DeveRetornarErroGenerico_QuandoSemJwtError() throws IOException {
        // Arrange
        when(request.getAttribute("jwt.error")).thenReturn(null);
        when(request.getAttribute("jwt.error.message")).thenReturn("Mensagem personalizada");
        when(authException.getMessage()).thenReturn("Erro de autenticação");

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(objectMapper).writeValueAsString(any(ErrorResponseDTO.class));
    }

    @Test
    void commence_DeveRetornarErroGenerico_QuandoSemMensagemPersonalizada() throws IOException {
        // Arrange
        when(request.getAttribute("jwt.error")).thenReturn(null);
        when(request.getAttribute("jwt.error.message")).thenReturn(null);
        when(authException.getMessage()).thenReturn("Erro de autenticação");

        // Act
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(objectMapper).writeValueAsString(any(ErrorResponseDTO.class));
    }
} 