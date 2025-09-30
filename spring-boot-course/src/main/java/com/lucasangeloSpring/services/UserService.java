package com.lucasangeloSpring.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lucasangeloSpring.models.User;
import com.lucasangeloSpring.models.dto.UserCreateDTO;
import com.lucasangeloSpring.models.dto.UserUpdateDTO;
import com.lucasangeloSpring.models.enums.ProfileEnum;
import com.lucasangeloSpring.repositories.UserRepository;
import com.lucasangeloSpring.security.UserSpringSecurity;
import com.lucasangeloSpring.services.exceptions.AuthorizationException;
import com.lucasangeloSpring.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired // como se fosse o construtor
    private UserRepository userRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = authenticated();

        if(userSpringSecurity == null || (!userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))) {
            throw new AuthorizationException("Acesso negado");
        }

        Optional<User> user = this.userRepository.findById(id); // Pode ou não receber um usuário - ai vem como vazio caso nao tenha o usuario solicitado - THIS para usar da classe, não do método que está dentro
        return user.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado! Id: " + id)); // Se não encontrar, lança uma exceção
    }

    @Transactional // usar sempre que vai fazer um insert, update ou delete - controle melhor do que está acontencendo na aplicação - "ou faz tudo ou faz nada" (salva tudo, não salva pela metade)
    public User create(User obj) {
        obj.setId(null); // Garantir que o ID é nulo para criar um novo usuário
        obj.setPassword(bCryptPasswordEncoder.encode(obj.getPassword())); // Criptografa a senha antes de salvar no banco
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet())); // Adiciona o perfil USER por padrão quando criar um novo usuário
        obj = this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId()); // garantir que o usuário existe
        newObj.setPassword(obj.getPassword()); // atualiza a senha
        newObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword())); // Criptografa a senha antes de salvar no banco
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id); // Verifica se o usuário existe
        
        try {
            this.userRepository.deleteById(id); // Deleta o usuário pelo ID
        } catch (Exception e) {
            throw new RuntimeException("Não é possível excluir, pois o usuário possui tarefas associadas.");
        }
    }

    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj) {
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj) {
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }
}
