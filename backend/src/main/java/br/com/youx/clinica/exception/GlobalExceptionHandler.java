package br.com.youx.clinica.exception;

import br.com.youx.clinica.dto.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.cors.CorsUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador global para tratamento de exceções
 * Intercepta e trata todas as exceções da aplicação de forma centralizada
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de token JWT expirado
     */
    @ExceptionHandler({TokenExpiredException.class, ExpiredJwtException.class})
    public ResponseEntity<ErrorResponseDTO> handleTokenExpired(
            Exception ex, HttpServletRequest request) {
        
        log.warn("Token expirado na requisição: {} - {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Token Expirado",
                "Seu token de acesso expirou. Faça login novamente.",
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Trata exceções de token JWT inválido
     */
    @ExceptionHandler({TokenInvalidException.class, JwtException.class})
    public ResponseEntity<ErrorResponseDTO> handleTokenInvalid(
            Exception ex, HttpServletRequest request) {
        
        log.warn("Token inválido na requisição: {} - {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Token Inválido",
                "Token de acesso inválido. Faça login novamente.",
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Trata exceções de token ausente
     */
    @ExceptionHandler(TokenMissingException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenMissing(
            TokenMissingException ex, HttpServletRequest request) {
        
        log.warn("Token ausente na requisição: {} - {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Token Ausente",
                "Token de acesso é obrigatório. Faça login para acessar este recurso.",
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Trata exceções de autenticação genéricas
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponseDTO> handleAuthentication(
            AuthenticationException ex, HttpServletRequest request) {
        
        log.warn("Erro de autenticação na requisição: {} - {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Erro de Autenticação",
                "Credenciais inválidas. Verifique suas informações de login.",
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Trata exceções de acesso negado (autorização)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        
        log.warn("Acesso negado na requisição: {} - {}", request.getRequestURI(), ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                "Você não possui permissão para acessar este recurso.",
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Trata erros de validação de campos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        log.warn("Erro de validação na requisição: {} - {}", request.getRequestURI(), ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = "Dados inválidos fornecidos. Verifique os campos: " + 
                        String.join(", ", errors.keySet());
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação",
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Trata violações de integridade de dados (ex: CPF duplicado)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        log.error("Violação de integridade de dados na requisição: {} - {}", 
                 request.getRequestURI(), ex.getMessage());
        
        String message = "Dados já existem no sistema ou violam regras de integridade.";
        
        // Verifica se é erro de CPF duplicado
        if (ex.getMessage() != null && ex.getMessage().contains("cpf")) {
            message = "Este CPF já está cadastrado no sistema.";
        }
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.CONFLICT.value(),
                "Conflito de Dados",
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Trata exceções genéricas não mapeadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneral(
            Exception ex, HttpServletRequest request) {
        
        log.error("Erro interno não tratado na requisição: {} - {}", 
                 request.getRequestURI(), ex.getMessage(), ex);
        
        // Se for requisição CORS preflight, retorna status apropriado
        if (CorsUtils.isPreFlightRequest(request)) {
            log.debug("Requisição CORS preflight detectada: {}", request.getRequestURI());
            return ResponseEntity.ok().build();
        }
        
        ErrorResponseDTO error = ErrorResponseDTO.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno",
                "Ocorreu um erro interno no servidor. Tente novamente mais tarde.",
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
} 