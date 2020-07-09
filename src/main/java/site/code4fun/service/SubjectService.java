package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Organization;
import site.code4fun.entity.Subject;
import site.code4fun.entity.dto.SubjectDTO;
import site.code4fun.repository.SubjectRepository;

@Service
public class SubjectService extends BaseService{
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	public List<SubjectDTO> getAll(){
		Map<Long, String> mapOrganization = getCurrentOrganization().stream().collect(Collectors.toMap(Organization::getId, Organization::getName));
		if(mapOrganization.keySet().size() == 0) return new ArrayList<>();
		
		List<Subject> lstSubject = subjectRepository.findByOrganizationIds(new ArrayList<>(mapOrganization.keySet()));
		List<SubjectDTO> lstDTO  = new ArrayList<SubjectDTO>();
		lstSubject.forEach(_item -> {
			SubjectDTO dto = SubjectDTO.fromEntity(_item);
			if(mapOrganization.containsKey(_item.getOrganizationId())) dto.setOrganizationName(mapOrganization.get(_item.getOrganizationId()));
			lstDTO.add(dto);
		});
		return lstDTO;
	}
	
	public List<SubjectDTO> getByOrgId(Long id){
		Map<Long, String> mapOrganization = getCurrentOrganization().stream().collect(Collectors.toMap(Organization::getId, Organization::getName));
		if(!mapOrganization.keySet().contains(id)) return new ArrayList<>();
		
		List<Subject> lstSubject = subjectRepository.findByOrganizationIds(Arrays.asList(id));
		List<SubjectDTO> lstDTO  = new ArrayList<SubjectDTO>();
		lstSubject.forEach(_item -> {
			SubjectDTO dto = SubjectDTO.fromEntity(_item);
			if(mapOrganization.containsKey(_item.getOrganizationId())) dto.setOrganizationName(mapOrganization.get(_item.getOrganizationId()));
			lstDTO.add(dto);
		});
		return lstDTO;
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
