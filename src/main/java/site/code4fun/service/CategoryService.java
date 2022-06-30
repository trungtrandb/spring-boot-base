package site.code4fun.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.code4fun.model.Category;
import site.code4fun.repository.CategoryRepository;


@Service
@Slf4j
public class CategoryService implements BaseService<Category, Long> {

    @Getter
    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }
}
