package br.com.youx.clinica.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Utilitários para operações de segurança
 * Facilita a obtenção de informações do usuário autenticado
 */
public class SecurityUtils {
    
    /**
     * Obtém o usuário autenticado atual
     * @return JwtAuthenticationToken se autenticado, Optional.empty() caso contrário
     */
    public static Optional<JwtAuthenticationToken> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.of(jwtAuth);
        }
        
        return Optional.empty();
    }
    
    /**
     * Obtém o ID do usuário autenticado
     * @return ID do usuário ou null se não autenticado
     */
    public static Long getCurrentUserId() {
        return getCurrentUser()
                .map(JwtAuthenticationToken::getUserId)
                .orElse(null);
    }
    
    /**
     * Obtém o CPF do usuário autenticado
     * @return CPF do usuário ou null se não autenticado
     */
    public static String getCurrentUserCpf() {
        return getCurrentUser()
                .map(JwtAuthenticationToken::getCpf)
                .orElse(null);
    }
    
    /**
     * Obtém o nome do usuário autenticado
     * @return Nome do usuário ou null se não autenticado
     */
    public static String getCurrentUserNome() {
        return getCurrentUser()
                .map(JwtAuthenticationToken::getNome)
                .orElse(null);
    }
    
    /**
     * Obtém a role do usuário autenticado
     * @return Role do usuário ou null se não autenticado
     */
    public static String getCurrentUserRole() {
        return getCurrentUser()
                .map(JwtAuthenticationToken::getRole)
                .orElse(null);
    }
    
    /**
     * Verifica se o usuário atual é médico
     * @return true se for médico, false caso contrário
     */
    public static boolean isCurrentUserMedico() {
        return "MEDICO".equals(getCurrentUserRole());
    }
    
    /**
     * Verifica se o usuário atual é enfermeira
     * @return true se for enfermeira, false caso contrário
     */
    public static boolean isCurrentUserEnfermeira() {
        return "ENFERMEIRA".equals(getCurrentUserRole());
    }
    
    /**
     * Verifica se há um usuário autenticado
     * @return true se autenticado, false caso contrário
     */
    public static boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }
} 