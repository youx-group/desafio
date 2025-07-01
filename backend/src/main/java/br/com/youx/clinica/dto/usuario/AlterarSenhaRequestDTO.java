package br.com.youx.clinica.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para requisição de alteração de senha
 * Contém a senha atual e a nova senha
 */
@Data
public class AlterarSenhaRequestDTO {
    
    @NotBlank(message = "Senha atual é obrigatória")
    private String senhaAtual;
    
    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "Nova senha deve ter no mínimo 6 caracteres")
    private String novaSenha;
} 