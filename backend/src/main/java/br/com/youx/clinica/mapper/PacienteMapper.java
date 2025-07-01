package br.com.youx.clinica.mapper;

import br.com.youx.clinica.dto.paciente.PacienteRequestDTO;
import br.com.youx.clinica.dto.paciente.PacienteResponseDTO;
import br.com.youx.clinica.model.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper para conversões entre Paciente e seus DTOs
 * Utiliza MapStruct para geração automática das implementações
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PacienteMapper {
    
    /**
     * Converte entidade Paciente para PacienteResponseDTO
     * @param paciente Entidade Paciente
     * @return DTO de resposta
     */
    PacienteResponseDTO toResponseDTO(Paciente paciente);
    
    /**
     * Converte lista de Paciente para lista de PacienteResponseDTO
     * @param pacientes Lista de entidades
     * @return Lista de DTOs de resposta
     */
    List<PacienteResponseDTO> toResponseDTOList(List<Paciente> pacientes);
    
    /**
     * Converte PacienteRequestDTO para entidade Paciente
     * @param requestDTO DTO de requisição
     * @return Entidade Paciente
     */
    Paciente toEntity(PacienteRequestDTO requestDTO);
    
    /**
     * Atualiza uma entidade Paciente existente com dados do DTO
     * @param requestDTO DTO com dados atualizados
     * @param paciente Entidade existente a ser atualizada
     */
    void updateEntityFromDTO(PacienteRequestDTO requestDTO, @MappingTarget Paciente paciente);
} 