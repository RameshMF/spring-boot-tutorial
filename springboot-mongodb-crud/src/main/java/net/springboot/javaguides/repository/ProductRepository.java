package net.springboot.javaguides.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.springboot.javaguides.model.Product;

public interface ProductRepository extends MongoRepository<Product, Long>{

}
