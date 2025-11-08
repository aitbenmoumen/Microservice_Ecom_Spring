package com.aitbenmoumen.billingservice;

import com.aitbenmoumen.billingservice.entities.Bill;
import com.aitbenmoumen.billingservice.entities.Customer;
import com.aitbenmoumen.billingservice.entities.Product;
import com.aitbenmoumen.billingservice.entities.ProductItem;
import com.aitbenmoumen.billingservice.feign.CustomerRestClient;
import com.aitbenmoumen.billingservice.feign.ProductRestClient;
import com.aitbenmoumen.billingservice.repositories.BillRepository;
import com.aitbenmoumen.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BillRepository repository, ProductItemRepository productRepository, ProductRestClient productRestClient, CustomerRestClient customerRestClient, BillRepository billRepository, ProductItemRepository productItemRepository) {

        return args -> {
            Collection<Customer> customers = customerRestClient.getAllCustomers().getContent();
            Collection<Product> products = productRestClient.getAllProducts().getContent();

            customers.forEach(customer -> {
                Bill bill = Bill.builder()
                        .billingDate(new Date())
                        .customerId(customer.getId())
                        .build();
                billRepository.save(bill);
                products.forEach(product -> {
                    ProductItem productItem = ProductItem.builder()
                            .bill(bill)
                            .productId(product.getId())
                            .quantity(new Random().nextInt(10)+1)
                            .unitPrice(product.getPrice())
                            .build();
                    productItemRepository.save(productItem);
                });
            });

        };
    }
}
