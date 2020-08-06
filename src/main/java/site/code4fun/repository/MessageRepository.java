package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Message;
import site.code4fun.entity.OutputMessage;

public interface MessageRepository extends JpaRepository<Message, Long>{
	
	@Query(value = "CALL getConversionByUserName(:userName);", nativeQuery = true)
	public List<OutputMessage> getListConversion(String userName);

}
