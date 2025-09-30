package com.lucasangeloSpring.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {
    
    private Long id;

    @NotBlank
    @Size(min = 8, max = 60, message = "A senha deve ter no mínimo 8 caracteres e no máximo 60.")
    private String password;
}
