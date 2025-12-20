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
            System.out.println("Waiting for customer-service and inventory-service to be available...");

            // Retry logic to wait for services to be available
            Collection<Customer> customers = null;
            Collection<Product> products = null;

            int maxRetries = 10;
            int retryCount = 0;

            // Try to fetch customers with retry
            while (customers == null && retryCount < maxRetries) {
                try {
                    customers = customerRestClient.getAllCustomers().getContent();
                    System.out.println("Successfully connected to customer-service");
                } catch (Exception e) {
                    retryCount++;
                    System.out.println("Attempt " + retryCount + "/" + maxRetries + " - Waiting for customer-service... " + e.getMessage());
                    if (retryCount < maxRetries) {
                        Thread.sleep(3000); // Wait 3 seconds before retry
                    } else {
                        System.err.println("Failed to connect to customer-service after " + maxRetries + " attempts");
                        throw e;
                    }
                }
            }

            // Try to fetch products with retry
            retryCount = 0;
            while (products == null && retryCount < maxRetries) {
                try {
                    products = productRestClient.getAllProducts().getContent();
                    System.out.println("Successfully connected to inventory-service");
                } catch (Exception e) {
                    retryCount++;
                    System.out.println("Attempt " + retryCount + "/" + maxRetries + " - Waiting for inventory-service... " + e.getMessage());
                    if (retryCount < maxRetries) {
                        Thread.sleep(3000); // Wait 3 seconds before retry
                    } else {
                        System.err.println("Failed to connect to inventory-service after " + maxRetries + " attempts");
                        throw e;
                    }
                }
            }

            // Process bills only if both services are available
            if (customers != null && products != null) {
                System.out.println("Creating bills...");
                Collection<Product> finalProducts = products;
                customers.forEach(customer -> {
                    Bill bill = Bill.builder()
                            .billingDate(new Date())
                            .customerId(customer.getId())
                            .build();
                    billRepository.save(bill);
                    finalProducts.forEach(product -> {
                        ProductItem productItem = ProductItem.builder()
                                .bill(bill)
                                .productId(product.getId())
                                .quantity(new Random().nextInt(10)+1)
                                .unitPrice(product.getPrice())
                                .build();
                        productItemRepository.save(productItem);
                    });
                });
                System.out.println("Bills created successfully!");
            }
        };
    }
}
