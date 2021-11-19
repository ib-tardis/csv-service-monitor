package com.assignment.monitorservice.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class ConfigProperties {

    @Value("${config.interval.sec}")
    @Getter
    public int interval;

    @Value("${config.datadir}")
    @Getter
    public String dataDir;

    @Value("${config.filename}")
    @Getter
    public String fileName;
}
