package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query("SELECT u FROM User u where u.username = :userName")
    public User findByUserName(String userName);
}
