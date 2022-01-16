package org.cynic.opsec_proxy.facade;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cynic.opsec_proxy.domain.ApplicationException;
import org.cynic.opsec_proxy.domain.dto.Video;
import org.cynic.opsec_proxy.service.VideosService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class VideosFacade {
    private final VideosService videosService;

    public VideosFacade(VideosService videosService) {
        this.videosService = videosService;
    }

    public Flux<String> all() {
        return videosService.all().map(Video::link);
    }

    public Mono<String> get(String title) {
        return videosService.all()
                .filter(it -> StringUtils.equalsIgnoreCase(it.title(), title))
                .collectList()
                .filter(CollectionUtils::isNotEmpty)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ApplicationException("error.video.not-found", title))))
                .filter(it -> CollectionUtils.size(it) == 1)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ApplicationException("error.video.non-unique", title))))
                .map(CollectionUtils::extractSingleton)
                .map(Video::link);
    }
}
