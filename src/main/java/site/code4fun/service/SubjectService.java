package site.code4fun.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Subject;
import site.code4fun.repository.SubjectRepository;

@Service
public class SubjectService extends BaseService{
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	public List<Subject> getAll(){
		Long id = null;
		return subjectRepository.findByOrganizationId(id);
	}
	
	public Subject insert(Subject s) {
		String status = s.getStatus().equals(Status.ACTIVE.getVal()) || s.getStatus().equals(Status.INACTIVE.getVal()) ? s.getStatus() : Status.ACTIVE.getVal();
		s.setStatus(status);
		s.setCreatedBy(getCurrentId());
		s.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		return subjectRepository.save(s);
	}
	
	public boolean delete(Long id) {
		subjectRepository.deleteById(id);
		return true;
	}
}
