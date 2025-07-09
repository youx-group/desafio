package br.com.youx.clinica.repository;

import br.com.youx.clinica.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de banco de dados da entidade Consulta
 * Fornece métodos CRUD básicos e consultas customizadas
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>, JpaSpecificationExecutor<Consulta> {
    
    /**
     * Busca consultas por paciente
     * @param pacienteId ID do paciente
     * @return Lista de consultas do paciente
     */
    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId ORDER BY c.data DESC")
    List<Consulta> findByPacienteId(@Param("pacienteId") Long pacienteId);
    
    /**
     * Busca consultas por médico
     * @param medicoId ID do médico
     * @return Lista de consultas do médico
     */
    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :medicoId ORDER BY c.data DESC")
    List<Consulta> findByMedicoId(@Param("medicoId") Long medicoId);
    
    /**
     * Busca consultas em um período específico
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Lista de consultas no período
     */
    @Query("SELECT c FROM Consulta c WHERE c.data BETWEEN :dataInicio AND :dataFim ORDER BY c.data")
    List<Consulta> findByDataBetween(@Param("dataInicio") LocalDateTime dataInicio, 
                                   @Param("dataFim") LocalDateTime dataFim);
    
    /**
     * Busca consultas futuras de um médico para verificar disponibilidade
     * @param medicoId ID do médico
     * @param data Data para verificação
     * @return Lista de consultas futuras do médico
     */
    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :medicoId AND c.data > :data ORDER BY c.data")
    List<Consulta> findConsultasFuturasByMedico(@Param("medicoId") Long medicoId, 
                                              @Param("data") LocalDateTime data);
    
    /**
     * Verifica se já existe consulta para o médico no horário especificado
     * @param medicoId ID do médico
     * @param data Data e hora da consulta
     * @return true se já existe consulta, false caso contrário
     */
    @Query("SELECT COUNT(c) > 0 FROM Consulta c WHERE c.medico.id = :medicoId AND c.data = :data")
    boolean existsByMedicoIdAndData(@Param("medicoId") Long medicoId, 
                                   @Param("data") LocalDateTime data);
} 