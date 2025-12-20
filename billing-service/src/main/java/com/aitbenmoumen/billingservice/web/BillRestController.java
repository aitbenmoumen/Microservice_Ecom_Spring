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

import java.util.List;

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

    @GetMapping(path = "/bills")
    public List<Bill> getAllBills(){
        List<Bill> bills = billRepository.findAll();
        bills.forEach(bill -> {
            bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
            bill.getProductItems().forEach(productItem -> {
                productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
            });
        });
        return bills;
    }

    @GetMapping(path = "/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        Bill bill = billRepository.findById(id).get();
        bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
            productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
        });
        return bill;
    }
}
