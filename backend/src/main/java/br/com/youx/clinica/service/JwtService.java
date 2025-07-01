package br.com.youx.clinica.service;

import br.com.youx.clinica.config.JwtProperties;
import br.com.youx.clinica.dto.usuario.UsuarioResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service para operações relacionadas a tokens JWT
 * Gera, valida e extrai informações dos tokens
 */
@Service
@RequiredArgsConstructor
public class JwtService {
    
    private final JwtProperties jwtProperties;
    
    /**
     * Gera um token JWT para um usuário
     * @param usuario Dados do usuário autenticado
     * @return Token JWT assinado
     */
    public String generateToken(UsuarioResponseDTO usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId());
        claims.put("nome", usuario.getNome());
        claims.put("cpf", usuario.getCpf());
        claims.put("role", usuario.getRole().name());
        
        return createToken(claims, usuario.getCpf());
    }
    
    /**
     * Cria um token JWT com as claims fornecidas
     * @param claims Claims a serem incluídas no token
     * @param subject Subject do token (geralmente CPF do usuário)
     * @return Token JWT assinado
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());
        
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * Valida se um token JWT é válido
     * @param token Token a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrai o CPF (subject) do token
     * @param token Token JWT
     * @return CPF do usuário
     */
    public String extractCpf(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extrai o ID do usuário do token
     * @param token Token JWT
     * @return ID do usuário
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }
    
    /**
     * Extrai o nome do usuário do token
     * @param token Token JWT
     * @return Nome do usuário
     */
    public String extractNome(String token) {
        return extractClaim(token, claims -> claims.get("nome", String.class));
    }
    
    /**
     * Extrai a role do usuário do token
     * @param token Token JWT
     * @return Role do usuário
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    
    /**
     * Extrai a data de expiração do token
     * @param token Token JWT
     * @return Data de expiração
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Verifica se o token está expirado
     * @param token Token JWT
     * @return true se expirado, false caso contrário
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Extrai uma claim específica do token
     * @param token Token JWT
     * @param claimsResolver Função para extrair a claim
     * @return Valor da claim
     */
    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extrai todas as claims do token
     * @param token Token JWT
     * @return Claims do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Obtém a chave de assinatura baseada no secret configurado
     * @return Chave secreta para assinatura
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
    
    /**
     * Extrai o token do header Authorization
     * @param authHeader Header Authorization
     * @return Token limpo (sem "Bearer ")
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
} 