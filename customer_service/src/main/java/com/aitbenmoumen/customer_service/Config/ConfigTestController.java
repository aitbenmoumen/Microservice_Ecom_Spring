package com.aitbenmoumen.customer_service.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigTestController {
    @Value("${global.params.p1}")
    private String p1;
    @Value("${global.params.p2}")
    private String p2;
    @Autowired
    private CustomerConfigParams customerConfigParams;

    @GetMapping("/config-test")
    public String getConfigParams() {
        return "Param X: " + p1 + ", Param Y: " + p2;
    }

}
