//package com.nttdata.credit_service.error;
//
//import lombok.Value;
//import org.slf4j.MDC;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import reactor.core.publisher.Mono;
//
//import java.time.Instant;
//
//@RestControllerAdvice
//public class GlobalErrorHandler {
//
//    // Clase interna que define el formato de la respuesta de error en JSON
//    @Value
//    static class ErrorBody {
//        Instant timestamp;
//        int status;
//        String error;
//        String message;
//        String corrId;
//        String reqId;
//        String path;
//    }
//
//    // Maneja IllegalArgumentException -> responde con 404
//    @ExceptionHandler(IllegalArgumentException.class)
//    public Mono<ResponseEntity<ErrorBody>> handleIllegalArgument(IllegalArgumentException ex) {
//        return build(HttpStatus.NOT_FOUND, ex);
//    }
//
//    // Maneja cualquier otra excepción -> responde con 500
//    @ExceptionHandler(Exception.class)
//    public Mono<org.springframework.http.ResponseEntity<ErrorBody>> handleAny(Exception ex) {
//        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex);
//    }
//
//    // Metodo común para construir la respuesta de error en JSON
//    private Mono<org.springframework.http.ResponseEntity<ErrorBody>> build(HttpStatus status, Exception ex) {
//        String corrId = MDC.get("corrId");// recupera el corrId del filtro de logging
//        String reqId  = MDC.get("reqId");// recupera el reqId del filtro de logging
//
//        // Log completo con stacktrace (para ver detalles en consola/logs)
//        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalErrorHandler.class);
//        logger.error("Unhandled error status={} corrId={} reqId={} err={}", status.value(), corrId, reqId, ex.toString(), ex);
//
//        // Construir objeto de error con todos los datos
//        ErrorBody body = new ErrorBody(
//                Instant.now(),
//                status.value(),
//                status.getReasonPhrase(),
//                ex.getMessage(),
//                corrId,
//                reqId,
//                "" // si deseas, puedes pasar el path desde un WebFilter o ServerRequest
//        );
//
//        // Devolver la respuesta JSON con el error y el status correspondiente
//        return Mono.just(org.springframework.http.ResponseEntity
//                .status(status)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(body));
//    }
//}
