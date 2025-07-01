package br.com.youx.clinica.repository;

import br.com.youx.clinica.enums.Role;
import br.com.youx.clinica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de banco de dados da entidade Usuario
 * Fornece métodos CRUD básicos e consultas customizadas
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca um usuário pelo CPF
     * @param cpf CPF do usuário
     * @return Optional com o usuário encontrado
     */
    Optional<Usuario> findByCpf(String cpf);
    
    /**
     * Busca usuários por role (tipo)
     * @param role Role do usuário (MEDICO ou ENFERMEIRA)
     * @return Lista de usuários com a role especificada
     */
    List<Usuario> findByRole(Role role);
    
    /**
     * Verifica se existe um usuário com o CPF informado
     * @param cpf CPF a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Busca apenas médicos para listagem em consultas
     * @return Lista de usuários que são médicos
     */
    @Query("SELECT u FROM Usuario u WHERE u.role = 'MEDICO'")
    List<Usuario> findMedicos();
} 