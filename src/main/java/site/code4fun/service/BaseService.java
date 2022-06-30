package site.code4fun.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import site.code4fun.exception.NotFoundException;
import site.code4fun.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static site.code4fun.constant.AppConstants.*;

public interface BaseService<T,ID> {

    BaseRepository<T, ID> getRepository();

    default Page<T> getPaging(int page, int size, String sortDir, String sort, String query) {
        long id = 0L;
        try {
            id = Long.parseLong(query);
        } catch (Exception ignored) {
        }
        page = page > 0 ? page - 1 : page;
        size = size > 0 ? size : DEFAULT_PAGE_SIZE;
        sort = isNotBlank(sort) ? sort : DEFAULT_SORT_COLUMN;
        sortDir = SORT_LIST.contains(sortDir) ? sortDir : SORT_LIST.get(0);
        PageRequest pageReq = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        return getRepository().findByIdOrNameContains(id, query, pageReq);
    }

    default T create(T role) {
        return getRepository().save(role);
    }

    default List<T> getAll() {
        return getRepository().findAll();
    }

    default void delete(ID id) {
        Optional<T> r = getRepository().findById(id);
        if (r.isPresent()) {
            getRepository().delete(r.get());
            return;
        }
        throw new NotFoundException();
    }

    default T getById(ID id) {
        return getRepository().findById(id).orElse(null);
    }
}
