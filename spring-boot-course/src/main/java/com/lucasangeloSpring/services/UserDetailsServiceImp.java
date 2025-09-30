package com.lucasangeloSpring.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lucasangeloSpring.models.User;
import com.lucasangeloSpring.repositories.UserRepository;
import com.lucasangeloSpring.security.UserSpringSecurity;

@Service
public class UserDetailsServiceImp implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);

        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserSpringSecurity(user.getId(), user.getUsername(), user.getPassword(), user.getProfiles());
    }
}
