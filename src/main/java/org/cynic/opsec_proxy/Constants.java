package org.cynic.opsec_proxy;

import java.time.Duration;

public final class Constants {
    private Constants() {
    }

    public static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration READ_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration WRITE_TIMEOUT = Duration.ofSeconds(5);

}
