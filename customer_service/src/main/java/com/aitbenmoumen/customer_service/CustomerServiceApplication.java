package com.aitbenmoumen.customer_service;

import com.aitbenmoumen.customer_service.entities.Customer;
import com.aitbenmoumen.customer_service.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(
                    Customer.builder()
                            .name("Ahmed")
                            .email("ahmed@gmail.com")
                    .build()
            );
            customerRepository.save(
                    Customer.builder()
                    .name("Salah")
                    .email("salah@gmail.com")
                    .build());
            customerRepository.save(
                    Customer.builder()
                            .name("Rita")
                            .email("rita@gmail.com")
                            .build());
            customerRepository.findAll().forEach(System.out::println);
        };
    }

}
