package br.com.youx.clinica.service;

import br.com.youx.clinica.config.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service para operações relacionadas a senhas
 * Utiliza BCrypt com salt personalizado para criptografia
 */
@Service
@RequiredArgsConstructor
public class PasswordService {
    
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    
    /**
     * Criptografa uma senha usando BCrypt com salt personalizado
     * @param rawPassword Senha em texto plano
     * @return Senha criptografada
     */
    public String encryptPassword(String rawPassword) {
        // Adiciona o salt personalizado à senha antes de criptografar
        String saltedPassword = rawPassword + securityProperties.getPasswordSalt();
        return passwordEncoder.encode(saltedPassword);
    }
    
    /**
     * Verifica se uma senha em texto plano corresponde à senha criptografada
     * @param rawPassword Senha em texto plano
     * @param encryptedPassword Senha criptografada armazenada
     * @return true se as senhas correspondem, false caso contrário
     */
    public boolean verifyPassword(String rawPassword, String encryptedPassword) {
        // Adiciona o salt personalizado à senha antes de verificar
        String saltedPassword = rawPassword + securityProperties.getPasswordSalt();
        return passwordEncoder.matches(saltedPassword, encryptedPassword);
    }
    
    /**
     * Verifica se uma senha atende aos critérios de segurança
     * @param password Senha a ser validada
     * @return true se a senha é válida, false caso contrário
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // Critérios de validação de senha
        return password.length() >= 6 && // Mínimo 6 caracteres
               password.length() <= 100 && // Máximo 100 caracteres
               !password.trim().equals(password) == false; // Não pode ter espaços no início/fim
    }
    
    /**
     * Gera uma mensagem de erro para senha inválida
     * @return Mensagem explicando os critérios de senha
     */
    public String getPasswordValidationMessage() {
        return "A senha deve ter entre 6 e 100 caracteres e não pode conter espaços no início ou fim";
    }
} 