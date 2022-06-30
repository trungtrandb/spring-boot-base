package site.code4fun.controller;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.code4fun.dto.PostDTO;
import site.code4fun.dto.request.PostRequest;
import site.code4fun.model.Category;
import site.code4fun.model.Post;
import site.code4fun.service.PostService;

import javax.validation.Valid;

import static com.googlecode.jmapper.api.JMapperAPI.global;
import static com.googlecode.jmapper.api.JMapperAPI.mappedClass;

@RestController
@RequestMapping("/posts")
@Slf4j
public class PostController {
	private final PostService service;

	@Autowired
	public PostController(PostService service) {
		this.service = service;
	}

	@GetMapping
	public Page<PostDTO> getAllPaging(@RequestParam(required = false, defaultValue = "0") int page,
									   @RequestParam(required = false, defaultValue = "0") int _limit,
									   @RequestParam(required = false) String sort,
									   @RequestParam(required = false) String sortDir,
									   @RequestParam(required = false, defaultValue = "") String q_like) {
		return service.getPaging(page, _limit, sortDir, sort, q_like).map(this::convertToDto);
	}

	@GetMapping("/{id}")
	public PostDTO getById(@PathVariable long id){
		return convertToDto(service.getById(id));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'DELETE')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable long id){
		service.delete(id);
	}

	@PostMapping
	public PostDTO create(@RequestBody @Valid PostRequest request){
		Post u = service.create(convertRequestToEntity(request));
		return convertToDto(u);
	}

	@PutMapping
	public PostDTO replace(@RequestBody @Valid PostRequest request){
		Post u = service.replace(convertRequestToEntity(request));
		return convertToDto(u);
	}

	@PatchMapping
	public PostDTO update(@RequestBody @Valid PostRequest request){
		Post u = service.update(convertRequestToEntity(request));
		return convertToDto(u);
	}

	private PostDTO convertToDto(Post source) {
		JMapperAPI jmapperApi = new JMapperAPI()
				.add(mappedClass(PostDTO.class).add(global()
						.excludedAttributes("categoryId", "categoryName")));
		JMapper<PostDTO, Post> mapper = new JMapper<>(PostDTO.class, Post.class, jmapperApi);
		PostDTO dto = mapper.getDestination(source);

		if (source.getCategory() != null){
			dto.setCategoryId(source.getCategory().getId());
			dto.setCategoryName(source.getCategory().getName());
		}
		return dto;
	}

	private Post convertRequestToEntity(PostRequest source) {
		JMapperAPI jmapperApi = new JMapperAPI()
				.add(mappedClass(Post.class).add(global()
						.excludedAttributes("category", "updated", "updatedBy", "created", "createdBy")));

		JMapper<Post, PostRequest> mapper = new JMapper<>(Post.class, PostRequest.class, jmapperApi);
		Post p = mapper.getDestination(source);
		if (source.getCategoryId() != null){
			Category c = new Category();
			c.setId(source.getCategoryId());
			p.setCategory(c);
		}

		return p;
	}
}
