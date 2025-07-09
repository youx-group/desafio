package br.com.youx.clinica.specification;

import br.com.youx.clinica.model.Consulta;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specification para filtros de consultas
 * Permite criar filtros dinâmicos para consultas usando JPA Criteria API
 */
public class ConsultaSpecification {
    
    /**
     * Filtra consultas por nome do paciente (busca parcial, case-insensitive)
     * @param nomePaciente Nome do paciente para filtrar
     * @return Specification para filtro por nome do paciente
     */
    public static Specification<Consulta> nomePacienteContains(String nomePaciente) {
        return (Root<Consulta> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (nomePaciente == null || nomePaciente.trim().isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(
                builder.lower(root.get("paciente").get("nome")),
                "%" + nomePaciente.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Filtra consultas por nome do médico (busca parcial, case-insensitive)
     * @param nomeMedico Nome do médico para filtrar
     * @return Specification para filtro por nome do médico
     */
    public static Specification<Consulta> nomeMedicoContains(String nomeMedico) {
        return (Root<Consulta> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (nomeMedico == null || nomeMedico.trim().isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(
                builder.lower(root.get("medico").get("nome")),
                "%" + nomeMedico.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Filtra consultas por período de data
     * @param dataInicio Data de início do período (opcional)
     * @param dataFim Data de fim do período (opcional)
     * @return Specification para filtro por período
     */
    public static Specification<Consulta> entreDatas(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return (Root<Consulta> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (dataInicio != null && dataFim != null) {
                return builder.between(root.get("data"), dataInicio, dataFim);
            } else if (dataInicio != null) {
                return builder.greaterThanOrEqualTo(root.get("data"), dataInicio);
            } else if (dataFim != null) {
                return builder.lessThanOrEqualTo(root.get("data"), dataFim);
            }
            return builder.conjunction();
        };
    }
    
    /**
     * Filtra consultas por ID do paciente
     * @param pacienteId ID do paciente
     * @return Specification para filtro por ID do paciente
     */
    public static Specification<Consulta> pacienteId(Long pacienteId) {
        return (Root<Consulta> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (pacienteId == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("paciente").get("id"), pacienteId);
        };
    }
    
    /**
     * Filtra consultas por ID do médico
     * @param medicoId ID do médico
     * @return Specification para filtro por ID do médico
     */
    public static Specification<Consulta> medicoId(Long medicoId) {
        return (Root<Consulta> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (medicoId == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("medico").get("id"), medicoId);
        };
    }
} 