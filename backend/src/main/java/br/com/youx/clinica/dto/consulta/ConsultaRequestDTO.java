package br.com.youx.clinica.dto.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para receber dados de criação/atualização de consulta
 * Contém validações dos campos obrigatórios
 */
@Data
public class ConsultaRequestDTO {
    
    @NotNull(message = "ID do paciente é obrigatório")
    private Long idPaciente;
    
    @NotNull(message = "ID do médico é obrigatório")
    private Long idMedico;
    
    @NotNull(message = "Data da consulta é obrigatória")
    @Future(message = "Data da consulta deve ser futura")
    private LocalDateTime data;
    
    @Size(max = 1000, message = "Observação deve ter no máximo 1000 caracteres")
    private String observacao;
} 