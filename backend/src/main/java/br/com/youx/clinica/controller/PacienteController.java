package br.com.youx.clinica.controller;

import br.com.youx.clinica.dto.paciente.PacienteRequestDTO;
import br.com.youx.clinica.dto.paciente.PacienteResponseDTO;
import br.com.youx.clinica.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciamento de pacientes
 * Fornece endpoints para operações CRUD de pacientes
 */
@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    
    private final PacienteService pacienteService;
    
    /**
     * Lista todos os pacientes
     * @return Lista de pacientes
     */
    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listarTodos() {
        List<PacienteResponseDTO> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }
    
    /**
     * Lista pacientes com paginação e filtros
     * @param busca Termo de busca para nome ou CPF (opcional)
     * @param pageable Informações de paginação (page, size, sort)
     * @return Página de pacientes
     */
    @GetMapping("/pagina")
    public ResponseEntity<Page<PacienteResponseDTO>> listarComPaginacao(
            @RequestParam(value = "busca", required = false) String busca,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        Page<PacienteResponseDTO> pacientes = pacienteService.listarComPaginacao(busca, pageable);
        return ResponseEntity.ok(pacientes);
    }
    
    /**
     * Busca paciente por ID
     * @param id ID do paciente
     * @return Paciente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<PacienteResponseDTO> paciente = pacienteService.buscarPorId(id);
        return paciente.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca pacientes por nome
     * @param nome Nome ou parte do nome do paciente
     * @return Lista de pacientes encontrados
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<PacienteResponseDTO> pacientes = pacienteService.buscarPorNome(nome);
        return ResponseEntity.ok(pacientes);
    }
    
    /**
     * Cria um novo paciente
     * @param pacienteRequest Dados do paciente
     * @return Paciente criado
     */
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> criar(@Valid @RequestBody PacienteRequestDTO pacienteRequest) {
        try {
            PacienteResponseDTO pacienteCriado = pacienteService.criar(pacienteRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(pacienteCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza um paciente existente
     * @param id ID do paciente
     * @param pacienteRequest Dados atualizados
     * @return Paciente atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(@PathVariable Long id, 
                                                        @Valid @RequestBody PacienteRequestDTO pacienteRequest) {
        try {
            PacienteResponseDTO pacienteAtualizado = pacienteService.atualizar(id, pacienteRequest);
            return ResponseEntity.ok(pacienteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Remove um paciente
     * @param id ID do paciente
     * @return Status da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            pacienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 