package com.lucasangeloSpring.security;

import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component // Indica que esta classe será gerenciada pelo Spring
public class JWTUtil {
    // Chave secreta usada para assinar/verificar o token, definida no application.properties
    @Value("${jwt.secret}")
    private String secret;

    // Tempo de expiração do token, definido no application.properties
    @Value("${jwt.expiration}")
    private Long expiration;

    // Gera a chave secreta a partir da string configurada
    private SecretKey getKeyBySecret() {
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
        return key;
    }

    // Gera um token JWT para o usuário informado
    public String genarateToken(String username){
        SecretKey key = getKeyBySecret(); // Obtém a chave secreta
        // Cria o token, define o usuário (subject) e a data de expiração
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + this.expiration)) // Data atual + tempo de expiração
            .signWith(key)
            .compact(); // Retorna o token JWT gerado
    }

    // Verifica se o token é válido (não expirou e tem usuário)
    public boolean isValidToken(String token) {
        Claims claims = getClaims(token); // Extrai as informações do token
    
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject(); // Usuário do token
            Date expirationDate = claims.getExpiration(); // Data de expiração
            Date now = new Date(System.currentTimeMillis()); // Data atual

            // Token é válido se tem usuário, data de expiração e não expirou
            if (Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate)) {
                return true;
            }
        }
        return false;
    }


    // Extrai as informações (claims) do token
    private Claims getClaims(String token) {
        SecretKey key = getKeyBySecret();
        
        try {
            // Valida e extrai o corpo do token (claims)
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            // Se não conseguir extrair (token inválido), retorna null
            return null;
        }
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token); // Extrai as informações do token
        if (Objects.nonNull(claims)) {
            return claims.getSubject(); // Retorna o usuário (subject) do token
        }
        return null; // Retorna null se não conseguir extrair as informações
    }
}
