package com.lucasangeloSpring.exceptions;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.lucasangeloSpring.services.exceptions.AuthorizationException;
import com.lucasangeloSpring.services.exceptions.DataBindingViolationException;
import com.lucasangeloSpring.services.exceptions.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER") // nome do logger que aparece no consele, falando de onde vem
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationFailureHandler {
    
    @Value("${server.error.include-exception}") // Injeta o valor da propriedade do application.properties na variável abaixo
    private boolean printStackTrace; // Define se o stack trace (detalhes do erro) será impresso nas respostas de erro, se false nao imprime nada - o bom é não imprimir em produção, pois pode vazar informações sensíveis
                                                                 // Se quiser ver o stack trace, basta colocar true no application.properties
                                                                 // server.error.include-exception=true

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // Define o status HTTP 422 para respostas de validação inválida
    protected @NonNull ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException methodArgumentNotValidException, 
        @NonNull HttpHeaders headers, 
        @NonNull HttpStatus status, 
        @NonNull WebRequest request) {

            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");

            for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
                errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResponseEntity.unprocessableEntity().body(errorResponse);
    }


    @ExceptionHandler(Exception.class) // Captura todas as exceções que não foram capturadas por outros handlers mais específicos - meio que um handler geralzao para quando surge um erro que nao tratamos
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Define o status HTTP 500 para respostas de erro interno do servidor
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        final String errorMessage = "Unknown error occurred";
        log.error(errorMessage, exception); // Loga o erro com a stack trace no console

        return buildErrorResponse(exception, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException exception, WebRequest request) {
        
        log.error("Failed to find the requested element", exception);
        String errorMessage = "Failed to find the requested element";
        
        return buildErrorResponse(
                exception,
                errorMessage,
                HttpStatus.NOT_FOUND,
                request);
    }


    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {

        log.error("Failed to save entity with associated data", dataBindingViolationException);
        String errorMessage = dataBindingViolationException.getMessage();
        
        return buildErrorResponse(
                dataBindingViolationException,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }


    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        
        
        ErrorResponse errorResponse = new ErrorResponse(
            httpStatus.value(),
            message
        );

        if(this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception)); // Adiciona o stack trace completo ao erro, se printStackTrace for true
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException dataIntegrityViolationException,
            WebRequest request) {

        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
        
        return buildErrorResponse(
                dataIntegrityViolationException,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {
        
        log.error("Failed to validate element", constraintViolationException);
        String errorMessage = constraintViolationException.getMessage();
        
        return buildErrorResponse(
                constraintViolationException,
                errorMessage,
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException authenticationException,
            WebRequest request) {
        log.error("Authentication error ", authenticationException);
        String errorMessage = authenticationException.getMessage();
        return buildErrorResponse(
                authenticationException,
                errorMessage,
                HttpStatus.UNAUTHORIZED,
                request);
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException accessDeniedException,
            WebRequest request) {
        log.error("Authorization error ", accessDeniedException);
        String errorMessage = accessDeniedException.getMessage();
        return buildErrorResponse(
                accessDeniedException,
                errorMessage,
                HttpStatus.FORBIDDEN,
                request);
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
            AuthenticationException exception) throws IOException, ServletException {

        Integer status = HttpStatus.UNAUTHORIZED.value(); // 401
        response.setStatus(status);
        response.setContentType("application/json"); // retorna json
        ErrorResponse errorResponse = new ErrorResponse(status, "Username or password are invalid");
        response.getWriter().write(errorResponse.toJson());
    }


    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthorizationException(
            AuthorizationException authorizationException,
            WebRequest request) {
        log.error("Authorization error ", authorizationException);
        String errorMessage = authorizationException.getMessage();
        return buildErrorResponse(
                authorizationException,
                errorMessage,
                HttpStatus.FORBIDDEN,
                request);
    }

}
