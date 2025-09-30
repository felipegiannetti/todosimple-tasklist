package com.lucasangeloSpring.services.exceptions;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) // Define o status HTTP 404 para respostas de erro de objeto não encontrado
public class ObjectNotFoundException extends EntityNotFoundException {

    public ObjectNotFoundException(String message) {
        super(message);
    }
    
}
