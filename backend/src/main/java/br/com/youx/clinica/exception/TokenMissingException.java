package br.com.youx.clinica.exception;

/**
 * Exceção lançada quando um token JWT é obrigatório mas não foi fornecido
 */
public class TokenMissingException extends RuntimeException {
    
    public TokenMissingException(String message) {
        super(message);
    }
    
    public TokenMissingException(String message, Throwable cause) {
        super(message, cause);
    }
} 