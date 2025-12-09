package com.aitbenmoumen.billingservice.web;

import com.aitbenmoumen.billingservice.entities.Bill;
import com.aitbenmoumen.billingservice.feign.CustomerRestClient;
import com.aitbenmoumen.billingservice.feign.ProductRestClient;
import com.aitbenmoumen.billingservice.repositories.BillRepository;
import com.aitbenmoumen.billingservice.repositories.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    BillRepository billRepository;
    ProductItemRepository productItemRepository;
    CustomerRestClient customerRestClient;
    ProductRestClient productRestClient;
    public BillRestController(BillRepository billRepository, ProductItemRepository productItemRepository,
                              CustomerRestClient customerRestClient, ProductRestClient productRestClient) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }
    @GetMapping(path = "/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        Bill b = billRepository.findById(id).get();
        b.setCustomer(customerRestClient.getCustomerById(b.getCustomerId()));
        return b;
    }
}
