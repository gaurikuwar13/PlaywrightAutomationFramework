package org.gauriKulkarni.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class LoginConfigManager {
    @Value("${eventHubLink}")
    private String eventHubLoginUrl;
    @Value("${eventHubUserName}")
    private String eventHubUsername;
    @Value("${eventHubPassword}")
    private String eventHubPassword;
}
