package br.com.youx.clinica.exception;

/**
 * Exceção lançada quando um token JWT é inválido
 */
public class TokenInvalidException extends RuntimeException {
    
    public TokenInvalidException(String message) {
        super(message);
    }
    
    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
} 