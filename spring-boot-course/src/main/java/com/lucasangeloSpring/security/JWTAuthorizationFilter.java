package com.lucasangeloSpring.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        String authorizationHeader = request.getHeader("Authorization");
        if(Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7); // Extrai o token removendo o "Bearer "
            UsernamePasswordAuthenticationToken auth = getAuthentication(token);

            if(Objects.nonNull(auth)){
                // Define o usuário autenticado no contexto de segurança do Spring
                // Isso permite que o Spring Security reconheça o usuário como autenticado
                // e aplique as regras de autorização configuradas
                // (como acesso a endpoints protegidos)
                // Se auth for null, o usuário não será autenticado
                // e não terá acesso a recursos protegidos
                // A requisição continuará sem autenticação
                // e poderá ser tratada por outros filtros ou pelo próprio endpoint
                // dependendo da configuração de segurança da aplicação
                // Por exemplo, pode resultar em um erro 401 Unauthorized se tentar acessar um recurso protegido
                // sem estar autenticado
                // Ou pode permitir acesso a recursos públicos que não exigem autenticação
                // A decisão final sobre o acesso é feita com base nas regras de segurança definidas na aplicação
                // e no estado de autenticação do usuário (se está autenticado ou não)
                // Portanto, é importante garantir que o token seja válido e que o usuário exista no sistema
                // para evitar problemas de segurança ou falhas na autenticação
                // Em resumo, este código autentica o usuário com base no token JWT fornecido na requisição
                // e define o contexto de segurança do Spring para permitir ou negar acesso a recursos protegidos
                // conforme as regras de segurança configuradas na aplicação.
                SecurityContextHolder.getContext().setAuthentication(auth); // autenticado em certo contexto
            }
        }
        filterChain.doFilter(request, response); // Continua a cadeia de filtros
    }


    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (this.jwtUtil.isValidToken(token)) {
            String username = this.jwtUtil.getUsername(token);
            UserDetails user = this.userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            return authenticatedUser;
        }
        return null;
    }
     
}
