package br.com.youx.clinica.dto.paciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para retornar dados de paciente nas respostas da API
 * Inclui todas as informações do paciente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponseDTO {
    
    private Long id;
    private String nome;
    private LocalDate dataDeNascimento;
    private String cpf;
    private String telefone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 