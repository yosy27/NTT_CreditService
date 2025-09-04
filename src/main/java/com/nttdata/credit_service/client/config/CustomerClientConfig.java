package com.nttdata.credit_service.client.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class CustomerClientConfig {

    @Bean
    WebClient customerWebClient(
            @Value("${external.customer.service.uri}") String base) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000) // conexión máx 2s
                .responseTimeout(Duration.ofSeconds(2))             // respuesta máx 2s
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(2))); // lectura máx 2s

        return WebClient.builder()
                .baseUrl(base)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
       // return WebClient.builder().baseUrl(base).build(); // p.ej. http://localhost:8081/api/v1
    }

}
