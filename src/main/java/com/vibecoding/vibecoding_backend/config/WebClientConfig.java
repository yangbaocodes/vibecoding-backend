package com.vibecoding.vibecoding_backend.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebClient配置类
 *
 * @author VibeCode Team
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final DifyConfig difyConfig;

    @Bean
    public WebClient.Builder webClientBuilder() {
        // 创建HttpClient，配置超时
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, difyConfig.getConnectTimeout())
                .responseTimeout(Duration.ofMillis(difyConfig.getReadTimeout()))
                .doOnConnected(conn -> 
                    conn.addHandlerLast(new ReadTimeoutHandler(difyConfig.getReadTimeout(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(difyConfig.getReadTimeout(), TimeUnit.MILLISECONDS)));

        log.info("WebClient配置完成 - 连接超时: {}ms, 读取超时: {}ms", 
            difyConfig.getConnectTimeout(), difyConfig.getReadTimeout());

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024)); // 10MB
    }
}