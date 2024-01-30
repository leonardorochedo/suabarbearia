package com.suabarbearia.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Credentials {

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;

    @Value("${certificate}")
    private String certificate;

    @Value("${sandbox}")
    private boolean sandbox;

    @Value("${debug}")
    private boolean debug;

    public Credentials() {

    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCertificate() {
        return certificate;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public boolean isDebug() {
        return debug;
    }

}
