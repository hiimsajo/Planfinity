package Planfinity.mainproject.ledger.service;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.ledger.categoryDto.CategoryPatchDto;
import Planfinity.mainproject.ledger.categoryDto.CategoryPostDto;
import Planfinity.mainproject.ledger.domain.Category;
import Planfinity.mainproject.ledger.repository.CategoryRepository;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoryService {
    private final MemberService memberService;
    private final CategoryRepository categoryRepository;

    public CategoryService(MemberService memberService, CategoryRepository categoryRepository) {
        this.memberService= memberService;
        this.categoryRepository = categoryRepository;
    }
    @Transactional
    public Category createCategory(CategoryPostDto postDto) {
        Member member = memberService.findMember();
        Category postedCategory = categoryRepository.save(postDto.toEntity(member));
        return postedCategory;
    }

    @Transactional
    public Category updateCategory(Long categoryId, CategoryPatchDto patchDto) {
        Member member =memberService.findMember();
        Category updatedCategory = existingCategory(categoryId);
        updatedCategory.setCategoryName(patchDto.getCategoryName());

        return updatedCategory;
    }
    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId){
        Member member = memberService.findMember();
        return existingCategory(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        Member member = memberService.findMember();
        List<Category> categories = this.categoryRepository.findAll();
        return categories;
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Member member = memberService.findMember();
        Category deletedCategory = existingCategory(categoryId);
        categoryRepository.delete(deletedCategory);
    }

    @Transactional
    public Category findByCategoryId(final Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public Category existingCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
        return existingCategory;
    }

}
