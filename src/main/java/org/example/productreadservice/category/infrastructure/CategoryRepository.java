package org.example.productreadservice.category.infrastructure;


import org.example.productreadservice.category.document.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}