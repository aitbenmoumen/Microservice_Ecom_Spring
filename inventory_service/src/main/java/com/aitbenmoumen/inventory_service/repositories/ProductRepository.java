package com.aitbenmoumen.inventory_service.repositories;

import com.aitbenmoumen.inventory_service.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "products")
public interface ProductRepository extends JpaRepository<Product, String> {
}
