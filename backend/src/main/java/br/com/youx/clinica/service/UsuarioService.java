package br.com.youx.clinica.service;

import br.com.youx.clinica.dto.usuario.UsuarioRequestDTO;
import br.com.youx.clinica.dto.usuario.UsuarioResponseDTO;
import br.com.youx.clinica.enums.Role;
import br.com.youx.clinica.mapper.UsuarioMapper;
import br.com.youx.clinica.model.Usuario;
import br.com.youx.clinica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciamento de usuários
 * Contém toda a lógica de negócio relacionada aos usuários
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordService passwordService;
    
    /**
     * Lista todos os usuários
     * @return Lista de usuários
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toResponseDTOList(usuarios);
    }
    
    /**
     * Busca usuário por ID
     * @param id ID do usuário
     * @return Usuário encontrado
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO);
    }
    
    /**
     * Lista apenas médicos
     * @return Lista de médicos
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarMedicos() {
        List<Usuario> medicos = usuarioRepository.findByRole(Role.MEDICO);
        return usuarioMapper.toResponseDTOList(medicos);
    }
    
    /**
     * Busca usuário por CPF
     * @param cpf CPF do usuário
     * @return Usuário encontrado
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf)
                .map(usuarioMapper::toResponseDTO);
    }
    
    /**
     * Cria um novo usuário
     * @param usuarioRequest Dados do usuário
     * @return Usuário criado
     * @throws IllegalArgumentException se CPF já existir ou senha for inválida
     */
    public UsuarioResponseDTO criar(UsuarioRequestDTO usuarioRequest) {
        // Valida se CPF já existe
        if (usuarioRepository.existsByCpf(usuarioRequest.getCpf())) {
            throw new IllegalArgumentException("CPF já está em uso: " + usuarioRequest.getCpf());
        }
        
        // Valida a senha
        if (!passwordService.isValidPassword(usuarioRequest.getSenha())) {
            throw new IllegalArgumentException(passwordService.getPasswordValidationMessage());
        }
        
        Usuario usuario = usuarioMapper.toEntity(usuarioRequest);
        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordService.encryptPassword(usuarioRequest.getSenha());
        usuario.setSenha(senhaCriptografada);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }
    
    /**
     * Atualiza um usuário existente
     * @param id ID do usuário
     * @param usuarioRequest Dados atualizados
     * @return Usuário atualizado
     * @throws IllegalArgumentException se usuário não existir, CPF já estiver em uso ou senha for inválida
     */
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO usuarioRequest) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
        
        // Verifica se o CPF não está sendo usado por outro usuário
        Optional<Usuario> usuarioComMesmoCpf = usuarioRepository.findByCpf(usuarioRequest.getCpf());
        if (usuarioComMesmoCpf.isPresent() && !usuarioComMesmoCpf.get().getId().equals(id)) {
            throw new IllegalArgumentException("CPF já está em uso por outro usuário: " + usuarioRequest.getCpf());
        }
        
        // Valida a senha se foi fornecida
        if (!passwordService.isValidPassword(usuarioRequest.getSenha())) {
            throw new IllegalArgumentException(passwordService.getPasswordValidationMessage());
        }
        
        usuarioMapper.updateEntityFromDTO(usuarioRequest, usuarioExistente);
        // Criptografa a nova senha antes de salvar
        String senhaCriptografada = passwordService.encryptPassword(usuarioRequest.getSenha());
        usuarioExistente.setSenha(senhaCriptografada);
        
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioAtualizado);
    }
    
    /**
     * Remove um usuário
     * @param id ID do usuário
     * @throws IllegalArgumentException se usuário não existir
     */
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    /**
     * Verifica se um usuário existe
     * @param id ID do usuário
     * @return true se existir, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }
    
    /**
     * Verifica se um usuário é médico
     * @param id ID do usuário
     * @return true se for médico, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean ehMedico(Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> usuario.getRole() == Role.MEDICO)
                .orElse(false);
    }
    
    /**
     * Valida as credenciais de login de um usuário
     * @param cpf CPF do usuário
     * @param senha Senha em texto plano
     * @return true se as credenciais são válidas, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean validarCredenciais(String cpf, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByCpf(cpf);
        if (usuario.isPresent()) {
            return passwordService.verifyPassword(senha, usuario.get().getSenha());
        }
        return false;
    }
    
    /**
     * Busca usuário por CPF e valida sua senha
     * @param cpf CPF do usuário
     * @param senha Senha em texto plano
     * @return Usuário se credenciais válidas, Optional.empty() caso contrário
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioResponseDTO> autenticar(String cpf, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByCpf(cpf);
        if (usuario.isPresent() && passwordService.verifyPassword(senha, usuario.get().getSenha())) {
            return Optional.of(usuarioMapper.toResponseDTO(usuario.get()));
        }
        return Optional.empty();
    }
    
    /**
     * Altera a senha de um usuário
     * @param id ID do usuário
     * @param senhaAtual Senha atual para validação
     * @param novaSenha Nova senha
     * @throws IllegalArgumentException se usuário não existir, senha atual incorreta ou nova senha inválida
     */
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
        
        // Verifica se a senha atual está correta
        if (!passwordService.verifyPassword(senhaAtual, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }
        
        // Valida a nova senha
        if (!passwordService.isValidPassword(novaSenha)) {
            throw new IllegalArgumentException(passwordService.getPasswordValidationMessage());
        }
        
        // Criptografa e salva a nova senha
        String novaSenhaCriptografada = passwordService.encryptPassword(novaSenha);
        usuario.setSenha(novaSenhaCriptografada);
        usuarioRepository.save(usuario);
    }
} 