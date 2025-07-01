package br.com.youx.clinica.enums;

/**
 * Enum que define os tipos de papel (role) dos usuários no sistema
 * Representa os diferentes tipos de profissionais que podem acessar o sistema
 */
public enum Role {
    MEDICO("Médico"),
    ENFERMEIRA("Enfermeira");
    
    private final String descricao;
    
    Role(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
} 