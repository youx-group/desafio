package br.com.youx.clinica.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Token de autenticação customizado para JWT
 * Estende UsernamePasswordAuthenticationToken com informações adicionais do usuário
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final Long userId;
    private final String nome;
    private final String role;
    
    /**
     * Construtor do token JWT com informações completas do usuário
     * @param cpf CPF do usuário (principal)
     * @param authorities Authorities/roles do usuário
     * @param userId ID do usuário
     * @param nome Nome do usuário
     * @param role Role do usuário
     */
    public JwtAuthenticationToken(String cpf, 
                                  Collection<? extends GrantedAuthority> authorities,
                                  Long userId, 
                                  String nome, 
                                  String role) {
        super(cpf, null, authorities);
        this.userId = userId;
        this.nome = nome;
        this.role = role;
    }
    
    /**
     * Obtém o ID do usuário
     * @return ID do usuário
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * Obtém o nome do usuário
     * @return Nome do usuário
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Obtém a role do usuário
     * @return Role do usuário
     */
    public String getRole() {
        return role;
    }
    
    /**
     * Obtém o CPF do usuário (principal)
     * @return CPF do usuário
     */
    public String getCpf() {
        return (String) getPrincipal();
    }
} 