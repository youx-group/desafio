package br.com.youx.clinica.service;

import br.com.youx.clinica.dto.paciente.PacienteRequestDTO;
import br.com.youx.clinica.dto.paciente.PacienteResponseDTO;
import br.com.youx.clinica.mapper.PacienteMapper;
import br.com.youx.clinica.model.Paciente;
import br.com.youx.clinica.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciamento de pacientes
 * Contém toda a lógica de negócio relacionada aos pacientes
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PacienteService {
    
    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    
    /**
     * Lista todos os pacientes
     * @return Lista de pacientes
     */
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listarTodos() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        return pacienteMapper.toResponseDTOList(pacientes);
    }
    
    /**
     * Busca paciente por ID
     * @param id ID do paciente
     * @return Paciente encontrado
     */
    @Transactional(readOnly = true)
    public Optional<PacienteResponseDTO> buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::toResponseDTO);
    }
    
    /**
     * Busca paciente por CPF
     * @param cpf CPF do paciente
     * @return Paciente encontrado
     */
    @Transactional(readOnly = true)
    public Optional<PacienteResponseDTO> buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .map(pacienteMapper::toResponseDTO);
    }
    
    /**
     * Busca pacientes por nome (busca parcial)
     * @param nome Nome ou parte do nome do paciente
     * @return Lista de pacientes encontrados
     */
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> buscarPorNome(String nome) {
        List<Paciente> pacientes = pacienteRepository.findByNomeContainingIgnoreCase(nome);
        return pacienteMapper.toResponseDTOList(pacientes);
    }
    
    /**
     * Busca pacientes por telefone
     * @param telefone Telefone do paciente
     * @return Lista de pacientes encontrados
     */
    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> buscarPorTelefone(String telefone) {
        List<Paciente> pacientes = pacienteRepository.findByTelefone(telefone);
        return pacienteMapper.toResponseDTOList(pacientes);
    }
    
    /**
     * Cria um novo paciente
     * @param pacienteRequest Dados do paciente
     * @return Paciente criado
     * @throws IllegalArgumentException se CPF já existir
     */
    public PacienteResponseDTO criar(PacienteRequestDTO pacienteRequest) {
        // Valida se CPF já existe
        if (pacienteRepository.existsByCpf(pacienteRequest.getCpf())) {
            throw new IllegalArgumentException("CPF já está em uso: " + pacienteRequest.getCpf());
        }
        
        Paciente paciente = pacienteMapper.toEntity(pacienteRequest);
        Paciente pacienteSalvo = pacienteRepository.save(paciente);
        return pacienteMapper.toResponseDTO(pacienteSalvo);
    }
    
    /**
     * Atualiza um paciente existente
     * @param id ID do paciente
     * @param pacienteRequest Dados atualizados
     * @return Paciente atualizado
     * @throws IllegalArgumentException se paciente não existir ou CPF já estiver em uso
     */
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO pacienteRequest) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com ID: " + id));
        
        // Verifica se o CPF não está sendo usado por outro paciente
        Optional<Paciente> pacienteComMesmoCpf = pacienteRepository.findByCpf(pacienteRequest.getCpf());
        if (pacienteComMesmoCpf.isPresent() && !pacienteComMesmoCpf.get().getId().equals(id)) {
            throw new IllegalArgumentException("CPF já está em uso por outro paciente: " + pacienteRequest.getCpf());
        }
        
        pacienteMapper.updateEntityFromDTO(pacienteRequest, pacienteExistente);
        Paciente pacienteAtualizado = pacienteRepository.save(pacienteExistente);
        return pacienteMapper.toResponseDTO(pacienteAtualizado);
    }
    
    /**
     * Remove um paciente
     * @param id ID do paciente
     * @throws IllegalArgumentException se paciente não existir
     */
    public void deletar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Paciente não encontrado com ID: " + id);
        }
        pacienteRepository.deleteById(id);
    }
    
    /**
     * Verifica se um paciente existe
     * @param id ID do paciente
     * @return true se existir, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return pacienteRepository.existsById(id);
    }
} 