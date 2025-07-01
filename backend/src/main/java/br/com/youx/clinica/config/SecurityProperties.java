package br.com.youx.clinica.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propriedades de configuração de segurança da aplicação
 * Carrega as configurações do BCrypt e salt do application.yml
 */
@Data
@Component
@ConfigurationProperties(prefix = "clinica.security")
public class SecurityProperties {
    
    /**
     * Salt usado para criptografia de senhas
     */
    private String passwordSalt;
    
    /**
     * Força do algoritmo BCrypt (4-31, recomendado: 10-12)
     */
    private int bcryptStrength = 12;
} 