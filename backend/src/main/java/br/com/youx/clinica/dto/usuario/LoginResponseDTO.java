package br.com.youx.clinica.dto.usuario;

import br.com.youx.clinica.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para resposta de login
 * Inclui o token JWT e informações básicas do usuário autenticado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    /**
     * Token JWT para autenticação
     */
    private String token;
    
    /**
     * Tipo do token (Bearer)
     */
    private String type = "Bearer";
    
    /**
     * Informações do usuário autenticado
     */
    private UsuarioInfo usuario;
    
    /**
     * Construtor com token e dados do usuário
     * @param token Token JWT
     * @param usuario Dados do usuário
     */
    public LoginResponseDTO(String token, UsuarioResponseDTO usuario) {
        this.token = token;
        this.type = "Bearer";
        this.usuario = new UsuarioInfo(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getRole()
        );
    }
    
    /**
     * Classe interna com informações básicas do usuário
     * Não inclui timestamps para resposta mais limpa
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioInfo {
        private Long id;
        private String nome;
        private String cpf;
        private Role role;
    }
} 