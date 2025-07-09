package br.com.youx.clinica.service;

import br.com.youx.clinica.dto.consulta.ConsultaRequestDTO;
import br.com.youx.clinica.dto.consulta.ConsultaResponseDTO;
import br.com.youx.clinica.mapper.ConsultaMapper;
import br.com.youx.clinica.model.Consulta;
import br.com.youx.clinica.model.Paciente;
import br.com.youx.clinica.model.Usuario;
import br.com.youx.clinica.repository.ConsultaRepository;
import br.com.youx.clinica.repository.PacienteRepository;
import br.com.youx.clinica.repository.UsuarioRepository;
import br.com.youx.clinica.specification.ConsultaSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciamento de consultas
 * Contém toda a lógica de negócio relacionada às consultas médicas
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ConsultaService {
    
    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConsultaMapper consultaMapper;
    
    /**
     * Busca paginada de consultas com filtros
     * @param nomePaciente Nome do paciente para filtrar (opcional)
     * @param nomeMedico Nome do médico para filtrar (opcional)
     * @param dataInicio Data de início do período (opcional)
     * @param dataFim Data de fim do período (opcional)
     * @param pacienteId ID do paciente (opcional)
     * @param medicoId ID do médico (opcional)
     * @param page Número da página (padrão: 0)
     * @param size Tamanho da página (padrão: 10)
     * @param sort Campo para ordenação (padrão: data)
     * @param direction Direção da ordenação (padrão: desc)
     * @return Página de consultas filtradas
     */
    @Transactional(readOnly = true)
    public Page<ConsultaResponseDTO> buscarPaginadoComFiltros(
            String nomePaciente,
            String nomeMedico,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Long pacienteId,
            Long medicoId,
            int page,
            int size,
            String sort,
            String direction
    ) {
        // Configura ordenação
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(sortDirection, sort);
        
        // Configura paginação
        Pageable pageable = PageRequest.of(page, size, sortBy);
        
        // Constrói filtros com Specification
        Specification<Consulta> spec = Specification.where(null);
        
        if (nomePaciente != null && !nomePaciente.trim().isEmpty()) {
            spec = spec.and(ConsultaSpecification.nomePacienteContains(nomePaciente));
        }
        
        if (nomeMedico != null && !nomeMedico.trim().isEmpty()) {
            spec = spec.and(ConsultaSpecification.nomeMedicoContains(nomeMedico));
        }
        
        if (dataInicio != null || dataFim != null) {
            spec = spec.and(ConsultaSpecification.entreDatas(dataInicio, dataFim));
        }
        
        if (pacienteId != null) {
            spec = spec.and(ConsultaSpecification.pacienteId(pacienteId));
        }
        
        if (medicoId != null) {
            spec = spec.and(ConsultaSpecification.medicoId(medicoId));
        }
        
        // Executa busca paginada
        Page<Consulta> consultas = consultaRepository.findAll(spec, pageable);
        
        // Converte para DTO
        return consultas.map(consultaMapper::toResponseDTO);
    }
    
    /**
     * Lista todas as consultas
     * @return Lista de consultas
     */
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarTodas() {
        List<Consulta> consultas = consultaRepository.findAll();
        return consultaMapper.toResponseDTOList(consultas);
    }
    
    /**
     * Busca consulta por ID
     * @param id ID da consulta
     * @return Consulta encontrada
     */
    @Transactional(readOnly = true)
    public Optional<ConsultaResponseDTO> buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(consultaMapper::toResponseDTO);
    }
    
    /**
     * Lista consultas por paciente
     * @param pacienteId ID do paciente
     * @return Lista de consultas do paciente
     */
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorPaciente(Long pacienteId) {
        List<Consulta> consultas = consultaRepository.findByPacienteId(pacienteId);
        return consultaMapper.toResponseDTOList(consultas);
    }
    
    /**
     * Lista consultas por médico
     * @param medicoId ID do médico
     * @return Lista de consultas do médico
     */
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorMedico(Long medicoId) {
        List<Consulta> consultas = consultaRepository.findByMedicoId(medicoId);
        return consultaMapper.toResponseDTOList(consultas);
    }
    
    /**
     * Lista consultas por período
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Lista de consultas no período
     */
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Consulta> consultas = consultaRepository.findByDataBetween(dataInicio, dataFim);
        return consultaMapper.toResponseDTOList(consultas);
    }
    
    /**
     * Lista consultas futuras de um médico
     * @param medicoId ID do médico
     * @return Lista de consultas futuras
     */
    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarConsultasFuturas(Long medicoId) {
        List<Consulta> consultas = consultaRepository.findConsultasFuturasByMedico(medicoId, LocalDateTime.now());
        return consultaMapper.toResponseDTOList(consultas);
    }
    
    /**
     * Cria uma nova consulta
     * @param consultaRequest Dados da consulta
     * @return Consulta criada
     * @throws IllegalArgumentException se paciente ou médico não existir, ou se já existir consulta no horário
     */
    public ConsultaResponseDTO criar(ConsultaRequestDTO consultaRequest) {
        // Valida se o paciente existe
        Paciente paciente = pacienteRepository.findById(consultaRequest.getIdPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com ID: " + consultaRequest.getIdPaciente()));
        
        // Valida se o médico existe
        Usuario medico = usuarioRepository.findById(consultaRequest.getIdMedico())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + consultaRequest.getIdMedico()));
        
        // Valida se o usuário é realmente médico
        if (!medico.getRole().name().equals("MEDICO")) {
            throw new IllegalArgumentException("O usuário informado não é um médico");
        }
        
        // Valida se já existe consulta para o médico no mesmo horário
        if (consultaRepository.existsByMedicoIdAndData(consultaRequest.getIdMedico(), consultaRequest.getData())) {
            throw new IllegalArgumentException("Já existe uma consulta agendada para este médico neste horário");
        }
        
        // Valida se a data da consulta é futura
        if (consultaRequest.getData().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data da consulta deve ser futura");
        }
        
        Consulta consulta = consultaMapper.toEntity(consultaRequest);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        
        Consulta consultaSalva = consultaRepository.save(consulta);
        return consultaMapper.toResponseDTO(consultaSalva);
    }
    
    /**
     * Atualiza uma consulta existente
     * @param id ID da consulta
     * @param consultaRequest Dados atualizados
     * @return Consulta atualizada
     * @throws IllegalArgumentException se consulta, paciente ou médico não existir
     */
    public ConsultaResponseDTO atualizar(Long id, ConsultaRequestDTO consultaRequest) {
        Consulta consultaExistente = consultaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada com ID: " + id));
        
        // Valida se o paciente existe
        Paciente paciente = pacienteRepository.findById(consultaRequest.getIdPaciente())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com ID: " + consultaRequest.getIdPaciente()));
        
        // Valida se o médico existe
        Usuario medico = usuarioRepository.findById(consultaRequest.getIdMedico())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + consultaRequest.getIdMedico()));
        
        // Valida se o usuário é realmente médico
        if (!medico.getRole().name().equals("MEDICO")) {
            throw new IllegalArgumentException("O usuário informado não é um médico");
        }
        
        // Valida conflito de horário apenas se mudou o médico ou a data
        boolean mudouMedicoOuData = !consultaExistente.getMedico().getId().equals(consultaRequest.getIdMedico()) ||
                                   !consultaExistente.getData().equals(consultaRequest.getData());
        
        if (mudouMedicoOuData && consultaRepository.existsByMedicoIdAndData(consultaRequest.getIdMedico(), consultaRequest.getData())) {
            throw new IllegalArgumentException("Já existe uma consulta agendada para este médico neste horário");
        }
        
        consultaMapper.updateEntityFromDTO(consultaRequest, consultaExistente);
        consultaExistente.setPaciente(paciente);
        consultaExistente.setMedico(medico);
        
        Consulta consultaAtualizada = consultaRepository.save(consultaExistente);
        return consultaMapper.toResponseDTO(consultaAtualizada);
    }
    
    /**
     * Remove uma consulta
     * @param id ID da consulta
     * @throws IllegalArgumentException se consulta não existir
     */
    public void deletar(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new IllegalArgumentException("Consulta não encontrada com ID: " + id);
        }
        consultaRepository.deleteById(id);
    }
    
    /**
     * Verifica se uma consulta existe
     * @param id ID da consulta
     * @return true se existir, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return consultaRepository.existsById(id);
    }
    
    /**
     * Verifica disponibilidade de horário para um médico
     * @param medicoId ID do médico
     * @param data Data e hora para verificar
     * @return true se disponível, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidade(Long medicoId, LocalDateTime data) {
        return !consultaRepository.existsByMedicoIdAndData(medicoId, data);
    }
} 