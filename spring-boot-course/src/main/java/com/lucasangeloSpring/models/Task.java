package com.lucasangeloSpring.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = Task.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data // gera getters e setters e o hashcode e equals
public class Task {
    public static final String TABLE_NAME = "task";

    
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Várias tarefas podem pertencer a um usuário
    @JoinColumn(name = "user_id", nullable = false, updatable = false) // Chave estrangeira para a tabela de usuários
    private User user;

    @Column(name = "description", length = 255, nullable = false)
    @NotBlank // so funciona para string
    @Size(min = 1, max = 255)
    private String description;
}
