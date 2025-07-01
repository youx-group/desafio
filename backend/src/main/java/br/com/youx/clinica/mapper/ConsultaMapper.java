package br.com.youx.clinica.mapper;

import br.com.youx.clinica.dto.consulta.ConsultaRequestDTO;
import br.com.youx.clinica.dto.consulta.ConsultaResponseDTO;
import br.com.youx.clinica.model.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper para conversões entre Consulta e seus DTOs
 * Utiliza MapStruct para geração automática das implementações
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, 
        uses = {UsuarioMapper.class, PacienteMapper.class})
public interface ConsultaMapper {
    
    /**
     * Converte entidade Consulta para ConsultaResponseDTO
     * @param consulta Entidade Consulta
     * @return DTO de resposta
     */
    @Mapping(source = "paciente", target = "paciente")
    @Mapping(source = "medico", target = "medico")
    ConsultaResponseDTO toResponseDTO(Consulta consulta);
    
    /**
     * Converte lista de Consulta para lista de ConsultaResponseDTO
     * @param consultas Lista de entidades
     * @return Lista de DTOs de resposta
     */
    List<ConsultaResponseDTO> toResponseDTOList(List<Consulta> consultas);
    
    /**
     * Converte ConsultaRequestDTO para entidade Consulta
     * Ignora os relacionamentos que serão definidos no service
     * @param requestDTO DTO de requisição
     * @return Entidade Consulta
     */
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "medico", ignore = true)
    Consulta toEntity(ConsultaRequestDTO requestDTO);
    
    /**
     * Atualiza uma entidade Consulta existente com dados do DTO
     * @param requestDTO DTO com dados atualizados
     * @param consulta Entidade existente a ser atualizada
     */
    @Mapping(target = "paciente", ignore = true)
    @Mapping(target = "medico", ignore = true)
    void updateEntityFromDTO(ConsultaRequestDTO requestDTO, @MappingTarget Consulta consulta);
} 