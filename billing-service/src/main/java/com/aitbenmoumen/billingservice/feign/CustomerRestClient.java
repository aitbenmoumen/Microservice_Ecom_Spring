package com.aitbenmoumen.billingservice.feign;

import com.aitbenmoumen.billingservice.entities.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


// this interface is used in order to communicate with the customer-service
@FeignClient(name = "customer-service")
public interface CustomerRestClient {
    @GetMapping("/api/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id);

    // --> ça va pas marcher car le retour est un _embedded...
//    @GetMapping("/api/customers")
//    public List<Customer> getAllCustomers();
    @GetMapping("/api/customers")
    public PagedModel<Customer> getAllCustomers();

}
