package org.cynic.opsec_proxy.controller.advice;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.cynic.opsec_proxy.domain.ApplicationException;
import org.cynic.opsec_proxy.domain.dto.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalControllerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<Error> unknownException(Throwable throwable) {
        LOGGER.error(StringUtils.EMPTY, throwable);

        return Mono.defer(() -> Mono.just(
                        new Error(
                                "error.unknown",
                                ExceptionUtils.getRootCauseMessage(throwable)
                        )
                )
        );
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Error> applicationException(ApplicationException applicationException) {
        LOGGER.error(StringUtils.EMPTY, applicationException);

        return Mono.defer(() -> Mono.just(
                        new Error(
                                applicationException.getCode(),
                                applicationException.getValues()
                        )
                )
        );
    }
}
