package site.code4fun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.code4fun.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUserName(String userName);

	Page<User> findByIdOrUserNameContainsOrFullNameContains(long id, String userName, String fullName, Pageable page);
}
