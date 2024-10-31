package org.example.productreadservice.category.consumer;

import org.example.productreadservice.category.document.Category;
import org.example.productreadservice.category.event.CategoryCreatedEvent;
import org.example.productreadservice.category.infrastructure.CategoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryEventConsumer {
	private final CategoryRepository categoryReadRepository;

	@KafkaListener(topics = "category-events", groupId = "product-read-service")
	public void handleCategoryCreated(CategoryCreatedEvent event) {
		Category categoryDocument = Category.builder()
			.categoryName(event.getCategoryName())
			.parentCategoryUuid(event.getParentCategoryUuid())
			.build();

		categoryReadRepository.save(categoryDocument);
	}
}