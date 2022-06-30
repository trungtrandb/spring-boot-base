package site.code4fun.controller;

import com.googlecode.jmapper.JMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.code4fun.dto.CategoryDTO;
import site.code4fun.dto.request.CategoryRequest;
import site.code4fun.model.Category;
import site.code4fun.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {
	private final CategoryService service;

	private final ModelMapper modelMapper;

	@Autowired
	public CategoryController(CategoryService service,
							  ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.service = service;
	}

	@GetMapping
	public Page<CategoryDTO> getAllPaging(@RequestParam(required = false, defaultValue = "0") int page,
									   @RequestParam(required = false, defaultValue = "0") int _limit,
									   @RequestParam(required = false) String sort,
									   @RequestParam(required = false) String sortDir,
									   @RequestParam(required = false, defaultValue = "") String q_like) {
		return service.getPaging(page, _limit, sortDir, sort, q_like).map(this::convertToDto);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'READ')")
	@GetMapping("/{id}")
	public CategoryDTO getUserById(@PathVariable long id){
		return convertToDto(service.getById(id));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'DELETE')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable long id){
		service.delete(id);
	}

	@PostMapping
	public CategoryDTO create(@RequestBody @Valid CategoryRequest request){
		Category u = service.create(convertRequestToEntity(request));
		return convertToDto(u);
	}

	private CategoryDTO convertToDto(Category source) {
		JMapper<CategoryDTO, Category> mapper = new JMapper<>(CategoryDTO.class, Category.class);
		return mapper.getDestination(source);
	}

	private Category convertRequestToEntity(CategoryRequest source) {
		return modelMapper.map(source, Category.class);
	}
}
