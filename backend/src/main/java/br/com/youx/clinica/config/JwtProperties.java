package br.com.youx.clinica.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propriedades de configuração do JWT
 * Carrega as configurações do JWT do application.yml
 */
@Data
@Component
@ConfigurationProperties(prefix = "clinica.jwt")
public class JwtProperties {
    
    /**
     * Chave secreta para assinatura dos tokens
     */
    private String secret;
    
    /**
     * Tempo de expiração do token em millisegundos
     */
    private long expiration = 86400000; // 24 horas
    
    /**
     * Issuer do token
     */
    private String issuer = "clinica-youx";
} 