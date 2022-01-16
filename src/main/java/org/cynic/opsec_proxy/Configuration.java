package org.cynic.opsec_proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

import java.net.URI;

@SpringBootConfiguration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {TypeExcludeFilter.class}),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {AutoConfigurationExcludeFilter.class})
})
@EnableCaching

//@EnableAutoConfiguration
@ImportAutoConfiguration({
//      WebFlux
        HttpHandlerAutoConfiguration.class,
        WebFluxAutoConfiguration.class,
        ReactiveWebServerFactoryAutoConfiguration.class,
        CodecsAutoConfiguration.class,

//      Jackson
        JacksonAutoConfiguration.class,
        CacheAutoConfiguration.class,

//      Web client
        WebClientAutoConfiguration.class
})
@EnableConfigurationProperties({
        Configuration.ProxyConfiguration.class
})
public class Configuration {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("videos");
    }

    @ConfigurationProperties(prefix = "proxy")
    @ConstructorBinding
    public record ProxyConfiguration(URI uri, String select, String attribute) {
    }


    @Autowired
    public void configure(ServerCodecConfigurer configurer, ObjectMapper objectMapper) {
        ServerCodecConfigurer.ServerDefaultCodecs serverDefaultCodecs = configurer.defaultCodecs();

        serverDefaultCodecs.jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
        serverDefaultCodecs.jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
    }
}
