package org.cynic.opsec_proxy.service;

import org.apache.commons.lang3.StringUtils;
import org.cynic.opsec_proxy.Configuration;
import org.cynic.opsec_proxy.domain.dto.Video;
import org.jsoup.Jsoup;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VideosService {

    private final WebClient.Builder webClientBuilder;
    private final Configuration.ProxyConfiguration proxyConfiguration;

    public VideosService(Configuration.ProxyConfiguration proxyConfiguration, WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.proxyConfiguration = proxyConfiguration;
    }

    @Cacheable("videos")
    public Flux<Video> all() {
        return webClientBuilder
                .build()
                .method(HttpMethod.GET)
                .uri(proxyConfiguration.uri())
                .retrieve()
                .bodyToMono(String.class)
                .flatMapIterable(this::parseResponse);
    }

    private List<Video> parseResponse(String html) {
        return Jsoup.parse(html)
                .body()
                .select(proxyConfiguration.select())
                .stream()
                .map(it -> new Video(StringUtils.trim(it.text()), it.attr(proxyConfiguration.attribute())))
                .collect(Collectors.toList());
    }
}
