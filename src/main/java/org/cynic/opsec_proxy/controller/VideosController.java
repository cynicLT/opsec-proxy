package org.cynic.opsec_proxy.controller;

import org.cynic.opsec_proxy.facade.VideosFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(path = "/videos")
public class VideosController {
    private final VideosFacade videosFacade;

    public VideosController(VideosFacade videosFacade) {
        this.videosFacade = videosFacade;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<String>> videos() {
        return videosFacade.all().collectList();
    }

    @GetMapping(path = "/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> video(@PathVariable String title) {
        return videosFacade.get(title);
    }
}
