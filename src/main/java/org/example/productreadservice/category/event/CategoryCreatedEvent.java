package org.example.productreadservice.category.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreatedEvent {
	private String categoryName;
	private String parentCategoryUuid;

	@Builder
	public CategoryCreatedEvent(String categoryName, String parentCategoryUuid) {
		this.categoryName = categoryName;
		this.parentCategoryUuid = parentCategoryUuid;
	}
}