package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.springboot.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
