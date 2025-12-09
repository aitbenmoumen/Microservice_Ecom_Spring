package com.aitbenmoumen.billingservice.web;

import com.aitbenmoumen.billingservice.entities.Bill;
import com.aitbenmoumen.billingservice.feign.CustomerRestClient;
import com.aitbenmoumen.billingservice.feign.ProductRestClient;
import com.aitbenmoumen.billingservice.repositories.BillRepository;
import com.aitbenmoumen.billingservice.repositories.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private CustomerRestClient customerRestClient;
    @Autowired
    private ProductRestClient productRestClient;

    @GetMapping(path = "/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        Bill b = billRepository.findById(id).get();
        b.setCustomer(customerRestClient.getCustomerById(b.getCustomerId()));
        return b;
    }
}
