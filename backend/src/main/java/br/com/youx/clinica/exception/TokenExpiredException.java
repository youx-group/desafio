package br.com.youx.clinica.exception;

/**
 * Exceção lançada quando um token JWT está expirado
 */
public class TokenExpiredException extends RuntimeException {
    
    public TokenExpiredException(String message) {
        super(message);
    }
    
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
} 