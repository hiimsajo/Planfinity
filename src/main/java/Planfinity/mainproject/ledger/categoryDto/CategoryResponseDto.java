package Planfinity.mainproject.ledger.categoryDto;

import Planfinity.mainproject.ledger.domain.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {

    @JsonProperty(value = "category_id")
    private Long categoryId;

    @JsonProperty(value = "category_name")
    private String categoryName;

    public CategoryResponseDto(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }

}

