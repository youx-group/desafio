package br.com.youx.clinica.mapper;

import br.com.youx.clinica.dto.usuario.UsuarioRequestDTO;
import br.com.youx.clinica.dto.usuario.UsuarioResponseDTO;
import br.com.youx.clinica.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper para conversões entre Usuario e seus DTOs
 * Utiliza MapStruct para geração automática das implementações
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {
    
    /**
     * Converte entidade Usuario para UsuarioResponseDTO
     * @param usuario Entidade Usuario
     * @return DTO de resposta
     */
    UsuarioResponseDTO toResponseDTO(Usuario usuario);
    
    /**
     * Converte lista de Usuario para lista de UsuarioResponseDTO
     * @param usuarios Lista de entidades
     * @return Lista de DTOs de resposta
     */
    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios);
    
    /**
     * Converte UsuarioRequestDTO para entidade Usuario
     * @param requestDTO DTO de requisição
     * @return Entidade Usuario
     */
    Usuario toEntity(UsuarioRequestDTO requestDTO);
    
    /**
     * Atualiza uma entidade Usuario existente com dados do DTO
     * @param requestDTO DTO com dados atualizados
     * @param usuario Entidade existente a ser atualizada
     */
    void updateEntityFromDTO(UsuarioRequestDTO requestDTO, @MappingTarget Usuario usuario);
} 