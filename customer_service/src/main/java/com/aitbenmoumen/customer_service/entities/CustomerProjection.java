package com.aitbenmoumen.customer_service.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "all", types = Customer.class)
public interface CustomerProjection {
    String getName();
    String getEmail();
}
// http://localhost:8081/customers?projection=all
