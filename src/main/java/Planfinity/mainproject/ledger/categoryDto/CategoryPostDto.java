package Planfinity.mainproject.ledger.categoryDto;

import Planfinity.mainproject.ledger.domain.Category;
import Planfinity.mainproject.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryPostDto {

    @JsonProperty(value = "category_name")
    private String categoryName;

    public Category toEntity(Member member) {
        return new Category(member, categoryName);
    }
}
