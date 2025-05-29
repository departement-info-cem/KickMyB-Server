package org.kickmyb.serveur.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.StandardCharsets;

/**
 * Cette classe indique à Spring Boot de
 * - intercepter toutes les exceptions qui n'auraient pas été attrapées : "@ExceptionHandler(value = {Exception.class})"
 * - indique le prendre le nom court de la classe d'Exception encodé en UTF-8 comme corps de la réponse : "bodyOfResponse = ex.getClass().getSimpleName()"
 * - ajoute un code d'erreur 400 soit BadRequest : "HttpStatus.BAD_REQUEST"
 */

@ControllerAdvice
public class ConfigGestionException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        // utile pour déboguer
        // ex.printStackTrace();
        // On prend le nom court de l'exception comme corps de la réponse HTTP 400 comme code
        String bodyOfResponse = ex.getClass().getSimpleName();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }
}