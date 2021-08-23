package com.vivy.springdoc;

import com.google.auto.service.AutoService;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;


@AutoService(BlockHoundIntegration.class)
public class SwaggerBlockHoundIntegration implements BlockHoundIntegration {

    @Override
    public void applyTo(BlockHound.Builder builder) {
        builder
                .allowBlockingCallsInside(
                        org.springdoc.core.OpenAPIService.class.getName(),
                        "build"
                );
    }
}


