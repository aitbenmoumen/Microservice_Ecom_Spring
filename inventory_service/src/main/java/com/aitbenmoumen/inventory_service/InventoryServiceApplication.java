package com.aitbenmoumen.inventory_service;

import com.aitbenmoumen.inventory_service.entities.Product;
import com.aitbenmoumen.inventory_service.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository repo ) {
        return args -> {
            for(int i = 0 ; i < 5 ; i++) {
                Product product = Product.builder()
                        .id(UUID.randomUUID().toString())
                        .name("product"+i)
                        .price(10*i)
                        .quantity(10*i+2)
                        .build();
                repo.save(product);
            }
            repo.findAll().forEach(System.out::println);
        };
    }
}
