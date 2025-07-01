package br.com.youx.clinica.controller;

import br.com.youx.clinica.dto.usuario.AlterarSenhaRequestDTO;
import br.com.youx.clinica.dto.usuario.LoginRequestDTO;
import br.com.youx.clinica.dto.usuario.LoginResponseDTO;
import br.com.youx.clinica.dto.usuario.UsuarioRequestDTO;
import br.com.youx.clinica.dto.usuario.UsuarioResponseDTO;
import br.com.youx.clinica.security.SecurityUtils;
import br.com.youx.clinica.service.JwtService;
import br.com.youx.clinica.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciamento de usuários
 * Fornece endpoints para operações CRUD de usuários do sistema
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    
    /**
     * Lista todos os usuários
     * @return Lista de usuários
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
    
    /**
     * Busca usuário por ID
     * @param id ID do usuário
     * @return Usuário encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<UsuarioResponseDTO> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Lista apenas médicos
     * @return Lista de médicos
     */
    @GetMapping("/medicos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarMedicos() {
        List<UsuarioResponseDTO> medicos = usuarioService.listarMedicos();
        return ResponseEntity.ok(medicos);
    }
    
    /**
     * Cria um novo usuário
     * @param usuarioRequest Dados do usuário
     * @return Usuário criado
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        try {
            UsuarioResponseDTO usuarioCriado = usuarioService.criar(usuarioRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Registra um novo usuário (endpoint público)
     * @param usuarioRequest Dados do usuário
     * @return Usuário criado
     */
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        try {
            UsuarioResponseDTO usuarioCriado = usuarioService.criar(usuarioRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza um usuário existente
     * @param id ID do usuário
     * @param usuarioRequest Dados atualizados
     * @return Usuário atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, 
                                                       @Valid @RequestBody UsuarioRequestDTO usuarioRequest) {
        try {
            UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizar(id, usuarioRequest);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Remove um usuário
     * @param id ID do usuário
     * @return Status da operação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            usuarioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Autentica um usuário com CPF e senha e retorna token JWT
     * @param loginRequest Dados de login
     * @return Token JWT e informações do usuário autenticado
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Optional<UsuarioResponseDTO> usuario = usuarioService.autenticar(
            loginRequest.getCpf(), 
            loginRequest.getSenha()
        );
        
        if (usuario.isPresent()) {
            // Gera o token JWT para o usuário autenticado
            String token = jwtService.generateToken(usuario.get());
            LoginResponseDTO response = new LoginResponseDTO(token, usuario.get());
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    /**
     * Verifica se as credenciais são válidas
     * @param loginRequest Dados de login
     * @return Status da validação
     */
    @PostMapping("/validar-credenciais")
    public ResponseEntity<Void> validarCredenciais(@Valid @RequestBody LoginRequestDTO loginRequest) {
        boolean credenciaisValidas = usuarioService.validarCredenciais(
            loginRequest.getCpf(), 
            loginRequest.getSenha()
        );
        
        return credenciaisValidas ? ResponseEntity.ok().build() 
                                 : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    /**
     * Altera a senha de um usuário
     * @param id ID do usuário
     * @param alterarSenhaRequest Dados para alteração de senha
     * @return Status da operação
     */
    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, 
                                            @Valid @RequestBody AlterarSenhaRequestDTO alterarSenhaRequest) {
        try {
            usuarioService.alterarSenha(
                id, 
                alterarSenhaRequest.getSenhaAtual(), 
                alterarSenhaRequest.getNovaSenha()
            );
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtém informações do usuário autenticado atual
     * @return Dados do usuário logado
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Optional<UsuarioResponseDTO> usuario = usuarioService.buscarPorId(userId);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Renova o token JWT do usuário autenticado
     * @return Novo token JWT
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDTO> refreshToken() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Optional<UsuarioResponseDTO> usuario = usuarioService.buscarPorId(userId);
        if (usuario.isPresent()) {
            String newToken = jwtService.generateToken(usuario.get());
            LoginResponseDTO response = new LoginResponseDTO(newToken, usuario.get());
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Endpoint para verificar se o token é válido
     * @return Status da validação
     */
    @GetMapping("/validate-token") 
    public ResponseEntity<Void> validateToken() {
        // Se chegou até aqui, o token é válido (filtro JWT já validou)
        return SecurityUtils.isAuthenticated() ? 
            ResponseEntity.ok().build() : 
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
} 