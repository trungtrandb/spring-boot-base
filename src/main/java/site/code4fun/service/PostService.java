package site.code4fun.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.code4fun.exception.NotFoundException;
import site.code4fun.model.Post;
import site.code4fun.repository.PostRepository;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
public class PostService implements BaseService<Post, Long>{

    @Getter
    private final PostRepository repository;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Post replace(Post post) {
        Optional<Post> p = repository.findById(post.getId());
        if (p.isPresent()){
            return repository.save(post);
        }
        throw new  NotFoundException("Object not found");
    }

    public Post update(Post source) {
        Optional<Post> p = repository.findById(source.getId());
        if (p.isPresent()){
            if (isNotBlank(source.getName())){
                p.get().setName(source.getName());
            }
            if (isNotBlank(source.getContent())){
                p.get().setContent(source.getContent());
            }
            if (isNotBlank(source.getStatus().toString())){
                p.get().setStatus(source.getStatus());
            }
            return repository.save(p.get());
        }
        throw new  NotFoundException("Object not found");
    }
}
