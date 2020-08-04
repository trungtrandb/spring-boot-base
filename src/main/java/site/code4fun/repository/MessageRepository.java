package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.code4fun.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{

}
