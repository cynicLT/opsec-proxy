package org.cynic.opsec_proxy.facade;

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

    public Flux<String> get(String title) {
        return videosService.all()
                .filter(it -> StringUtils.containsIgnoreCase(it.title(), title))
                .map(Video::link)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ApplicationException("error.video.not-found", title))));
    }
}
