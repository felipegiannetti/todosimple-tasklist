package com.lucasangeloSpring.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lucasangeloSpring.models.enums.ProfileEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSpringSecurity implements UserDetails{

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }
    public UserSpringSecurity(Long id, String username, String password, Set<ProfileEnum> profileEnum) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = profileEnum.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // conta nao expira - tipo data pra expirar
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // conta nao bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // credenciais (senha) nao expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // conta está ativa
    }

    public boolean hasRole(ProfileEnum profileEnum) {
        return getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
    } // verifica se o usuario tem o perfil (role) passado
    
}
