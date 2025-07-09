package br.com.youx.clinica.specification;

import br.com.youx.clinica.model.Paciente;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification para filtros de pacientes
 * Permite criar filtros dinâmicos para pacientes usando JPA Criteria API
 */
public class PacienteSpecification {
    
    /**
     * Filtra pacientes por nome (busca parcial, case-insensitive)
     * @param nome Nome do paciente para filtrar
     * @return Specification para filtro por nome
     */
    public static Specification<Paciente> nomeContains(String nome) {
        return (Root<Paciente> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (nome == null || nome.trim().isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(
                builder.lower(root.get("nome")),
                "%" + nome.toLowerCase() + "%"
            );
        };
    }
    
    /**
     * Filtra pacientes por CPF (busca parcial)
     * @param cpf CPF do paciente para filtrar
     * @return Specification para filtro por CPF
     */
    public static Specification<Paciente> cpfContains(String cpf) {
        return (Root<Paciente> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (cpf == null || cpf.trim().isEmpty()) {
                return builder.conjunction();
            }
            // Remove caracteres especiais do CPF para busca
            String cpfLimpo = cpf.replaceAll("[^0-9]", "");
            return builder.like(root.get("cpf"), "%" + cpfLimpo + "%");
        };
    }
    
    /**
     * Filtra pacientes por nome OU CPF (busca unificada)
     * @param busca Termo de busca que pode ser nome ou CPF
     * @return Specification para filtro por nome ou CPF
     */
    public static Specification<Paciente> nomeOuCpfContains(String busca) {
        return (Root<Paciente> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (busca == null || busca.trim().isEmpty()) {
                return builder.conjunction();
            }
            
            // Cria predicado para busca por nome (case-insensitive)
            Predicate nomeContains = builder.like(
                builder.lower(root.get("nome")),
                "%" + busca.toLowerCase() + "%"
            );
            
            // Cria predicado para busca por CPF (remove caracteres especiais)
            String cpfLimpo = busca.replaceAll("[^0-9]", "");
            Predicate cpfContains = builder.like(
                root.get("cpf"),
                "%" + cpfLimpo + "%"
            );
            
            // Retorna OR dos dois predicados
            return builder.or(nomeContains, cpfContains);
        };
    }
    
    /**
     * Filtra pacientes por telefone (busca parcial)
     * @param telefone Telefone do paciente para filtrar
     * @return Specification para filtro por telefone
     */
    public static Specification<Paciente> telefoneContains(String telefone) {
        return (Root<Paciente> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (telefone == null || telefone.trim().isEmpty()) {
                return builder.conjunction();
            }
            // Remove caracteres especiais do telefone para busca
            String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
            return builder.like(root.get("telefone"), "%" + telefoneLimpo + "%");
        };
    }
    
    /**
     * Filtra pacientes por ID específico
     * @param id ID do paciente
     * @return Specification para filtro por ID
     */
    public static Specification<Paciente> idEquals(Long id) {
        return (Root<Paciente> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (id == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("id"), id);
        };
    }
} 