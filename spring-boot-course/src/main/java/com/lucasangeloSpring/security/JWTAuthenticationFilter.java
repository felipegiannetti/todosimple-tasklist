package com.lucasangeloSpring.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasangeloSpring.exceptions.GlobalExceptionHandler;
import com.lucasangeloSpring.models.User;

// login e senha chegam aqui, se estiverem corretos, gera o token JWT
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        setAuthenticationFailureHandler(new GlobalExceptionHandler()); // Define o handler de falha de autenticação
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class); // Lê o usuário e senha do corpo da requisição

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList<>()); // Cria o token de autenticação
            Authentication authenticantion = authenticationManager.authenticate(authToken); // Tenta autenticar o usuário
            return authenticantion; // Retorna a autenticação se for bem sucedida
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException, ServletException {
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal(); // Pega o usuário autenticado
        
        String username = userSpringSecurity.getUsername(); // Pega o nome do usuário autenticado
        String token = this.jwtUtil.genarateToken(username); // Gera o token JWT para o usuário
        response.addHeader("Authorization", "Bearer " + token); // Adiciona o token no cabeçalho da respostas
        response.addHeader("access-control-expose-headers", "Authorization"); // Libera o cabeçalho Authorization para o front-end acessar
    }
}
