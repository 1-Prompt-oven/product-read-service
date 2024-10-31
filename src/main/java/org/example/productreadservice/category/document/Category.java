package org.example.productreadservice.category.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;

@Document(collection = "categories")
@Getter
@Builder
public class Category {
	@Id
	private String id;
	private String categoryName;
	private String parentCategoryUuid;
}
