package com.aitbenmoumen.customer_service.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "global.params")
public record CustomerConfigParams(int p1, int p2) {
}
