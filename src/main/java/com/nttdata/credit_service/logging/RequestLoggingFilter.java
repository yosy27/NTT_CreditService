//package com.nttdata.credit_service.logging;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
////Filtro para registrar logs de todas las peticiones y respuestas en WebFlux
////Genera o reutiliza un CorrelationId para seguir la petición
////Imprime en logs la entrada y la salida
////Si ocurre un error, también se imprime en el log con detalle
//@Component
//public class RequestLoggingFilter implements WebFilter {
//
//    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
//
//    // Claves para MDC (almacen temporal de datos en logs)
//    private static final String CORR_ID = "corrId";// Id de correlación
//    private static final String REQ_ID  = "reqId";// Id de la petición
//    private static final String HDR_CORR = "X-Correlation-Id"; // Header usado para pasar el corrId entre servicios
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        ServerHttpRequest req = exchange.getRequest();
//
//        // Obtener CorrelationId desde el header
//        String corrId = req.getHeaders().getFirst(HDR_CORR);
//        if (corrId == null || corrId.isBlank()) {
//            corrId = UUID.randomUUID().toString();
//        }
//        final String fCorrId = corrId;
//
//        // Obtener el RequestId que genera WebFlux automáticamente
//        final String fReqId = req.getId();
//
//        // Guardar algunos datos fijos de la petición para loguear
//        final long   fStart   = System.currentTimeMillis();
//        final String fMethod  = req.getMethod() != null ? req.getMethod().name() : "UNKNOWN";
//        final String fPath    = req.getURI().getPath();
//        final String fQuery   = req.getURI().getQuery() != null ? req.getURI().getQuery() : "-";
//        final int    fHdrSize = req.getHeaders().size();
//
//        // Guardar corrId y reqId en el exchange
//        // y agregar corrId a la respuesta
//        exchange.getAttributes().put(CORR_ID, fCorrId);
//        exchange.getAttributes().put(REQ_ID,  fReqId);
//        exchange.getResponse().getHeaders().add(HDR_CORR, fCorrId);
//
//        // Log de entrada
//        withMdc(fCorrId, fReqId, () ->
//                log.info(">> {} {} query={} headers={}", fMethod, fPath, fQuery, fHdrSize)
//        );
//
//        // Continuar cadena y loggear salida/errores
//        return chain.filter(exchange)
//                .doOnSuccess(v -> {
//                    // Si es exitoso, logueamos la respuesta normal
//                    final int status = exchange.getResponse().getStatusCode() != null
//                            ? exchange.getResponse().getStatusCode().value()
//                            : 200;
//                    final long took = System.currentTimeMillis() - fStart;
//                    withMdc(fCorrId, fReqId, () ->
//                            log.info("<< {} {} status={} took={}ms", fMethod, fPath, status, took)
//                    );
//                })
//                .doOnError(err -> {
//                    // Si hubo error, lo registramos con nivel ERROR
//                    final int status = exchange.getResponse().getStatusCode() != null
//                            ? exchange.getResponse().getStatusCode().value()
//                            : 500;
//                    final long took = System.currentTimeMillis() - fStart;
//                    withMdc(fCorrId, fReqId, () ->
//                            log.error("<< {} {} status={} took={}ms error={}",
//                                    fMethod, fPath, status, took, err.toString(), err)
//                    );
//                });
//    }
//
//    // Metodo auxiliar para ejecutar un bloque de código con los datos MDC cargados
//    // Limpia esos datos para no mezclarlos con otras peticiones
//    private static void withMdc(String corrId, String reqId, Runnable r) {
//        MDC.put(CORR_ID, corrId);
//        MDC.put(REQ_ID, reqId);
//        try { r.run(); }
//        finally {
//            MDC.remove(CORR_ID);
//            MDC.remove(REQ_ID);
//        }
//    }
//}
