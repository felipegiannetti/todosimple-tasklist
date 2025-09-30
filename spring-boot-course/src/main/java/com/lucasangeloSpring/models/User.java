package com.lucasangeloSpring.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.lucasangeloSpring.models.enums.ProfileEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {          

    public static final String TABLE_NAME = "user";

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Igual o auto_increment do MySQL
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotBlank
    // @NotBlank faz o NotNull e NotEmpty, mas também verifica se não é só espaço em branco
    @Size(min = 2, max = 100)
    private String username;

    @Column(name = "password", length = 60, nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY) // Não expõe a senha na serialização JSON (tipo não retorna a senha para o usuário e front-end)
    @NotBlank
    @Size(min = 8, max = 60, message = "A senha deve ter no mínimo 8 caracteres e no máximo 60.")
    private String password;

    @OneToMany(mappedBy = "user") // Um usuário pode ter várias tarefas - Quem é o dono das tasks (variável user)
    @JsonProperty(access = Access.WRITE_ONLY) // Não expõe as tasks na serialização JSON (tipo não retorna as tasks para o usuário e front-end)
    private List<Task> tasks = new ArrayList<Task>();

    @Column(name = "profile", nullable = false) 
    @ElementCollection(fetch = FetchType.EAGER) // Sempre que buscar o usuário, já traz os perfis junto - carrega junto
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Não expõe os perfis na serialização JSON (tipo não retorna os perfis para o usuário e front-end)
    @CollectionTable(name = "user_profiles")
    private Set<Integer> profiles = new HashSet<>(); // lista de valores unicos (set) que armazena os códigos dos perfis (enums) do usuário

    public Set<ProfileEnum> getProfiles() { // transforma a lista de Integers em lista de enums
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }
}
