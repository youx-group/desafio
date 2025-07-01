package br.com.youx.clinica.dto.consulta;

import br.com.youx.clinica.dto.paciente.PacienteResponseDTO;
import br.com.youx.clinica.dto.usuario.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para retornar dados de consulta nas respostas da API
 * Inclui informações completas do paciente e médico
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaResponseDTO {
    
    private Long id;
    private PacienteResponseDTO paciente;
    private UsuarioResponseDTO medico;
    private LocalDateTime data;
    private String observacao;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 