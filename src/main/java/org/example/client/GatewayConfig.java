package org.example.client;

import java.time.Duration;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.converters.DurationConverter;

@Sources("classpath:application.properties")
public interface GatewayConfig extends Config {
    @DefaultValue("100 ms")
    @ConverterClass(DurationConverter.class)
    Duration requestTimeout();

    @DefaultValue("${GRPC_TARGET_HOST}")
    String grpcTarget();
}
