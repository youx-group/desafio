package br.com.youx.clinica.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para requisição de login de usuário
 * Contém CPF e senha para autenticação
 */
@Data
public class LoginRequestDTO {
    
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 dígitos")
    private String cpf;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
} 