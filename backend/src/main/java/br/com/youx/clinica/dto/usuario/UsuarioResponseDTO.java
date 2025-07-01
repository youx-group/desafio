package br.com.youx.clinica.dto.usuario;

import br.com.youx.clinica.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para retornar dados de usuário nas respostas da API
 * Não inclui informações sensíveis como senha
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    
    private Long id;
    private String nome;
    private String cpf;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 