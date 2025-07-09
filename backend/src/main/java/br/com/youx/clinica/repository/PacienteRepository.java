package br.com.youx.clinica.repository;

import br.com.youx.clinica.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade Paciente
 * Fornece métodos CRUD básicos e consultas customizadas
 */
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {
    
    /**
     * Busca um paciente pelo CPF
     * @param cpf CPF do paciente
     * @return Optional com o paciente encontrado
     */
    Optional<Paciente> findByCpf(String cpf);
    
    /**
     * Verifica se existe um paciente com o CPF informado
     * @param cpf CPF a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Busca pacientes por nome (busca parcial, case insensitive)
     * @param nome Nome ou parte do nome do paciente
     * @return Lista de pacientes encontrados
     */
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Paciente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca pacientes por telefone
     * @param telefone Telefone do paciente
     * @return Lista de pacientes com o telefone informado
     */
    List<Paciente> findByTelefone(String telefone);
} 