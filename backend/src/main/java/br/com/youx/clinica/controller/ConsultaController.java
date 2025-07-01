package br.com.youx.clinica.controller;

import br.com.youx.clinica.dto.consulta.ConsultaRequestDTO;
import br.com.youx.clinica.dto.consulta.ConsultaResponseDTO;
import br.com.youx.clinica.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciamento de consultas
 * Fornece endpoints para operações CRUD de consultas médicas
 */
@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {
    
    private final ConsultaService consultaService;
    
    /**
     * Lista todas as consultas
     * @return Lista de consultas
     */
    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listarTodas() {
        List<ConsultaResponseDTO> consultas = consultaService.listarTodas();
        return ResponseEntity.ok(consultas);
    }
    
    /**
     * Busca consulta por ID
     * @param id ID da consulta
     * @return Consulta encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<ConsultaResponseDTO> consulta = consultaService.buscarPorId(id);
        return consulta.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Lista consultas por paciente
     * @param pacienteId ID do paciente
     * @return Lista de consultas do paciente
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<ConsultaResponseDTO> consultas = consultaService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(consultas);
    }
    
    /**
     * Lista consultas por médico
     * @param medicoId ID do médico
     * @return Lista de consultas do médico
     */
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorMedico(@PathVariable Long medicoId) {
        List<ConsultaResponseDTO> consultas = consultaService.listarPorMedico(medicoId);
        return ResponseEntity.ok(consultas);
    }
    
    /**
     * Lista consultas por período
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Lista de consultas no período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<ConsultaResponseDTO> consultas = consultaService.listarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(consultas);
    }
    
    /**
     * Cria uma nova consulta
     * @param consultaRequest Dados da consulta
     * @return Consulta criada
     */
    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> criar(@Valid @RequestBody ConsultaRequestDTO consultaRequest) {
        try {
            ConsultaResponseDTO consultaCriada = consultaService.criar(consultaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(consultaCriada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza uma consulta existente
     * @param id ID da consulta
     * @param consultaRequest Dados atualizados
     * @return Consulta atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> atualizar(@PathVariable Long id, 
                                                        @Valid @RequestBody ConsultaRequestDTO consultaRequest) {
        try {
            ConsultaResponseDTO consultaAtualizada = consultaService.atualizar(id, consultaRequest);
            return ResponseEntity.ok(consultaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Remove uma consulta
     * @param id ID da consulta
     * @return Status da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            consultaService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}