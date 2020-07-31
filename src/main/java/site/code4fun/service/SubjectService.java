package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Organization;
import site.code4fun.entity.Subject;
import site.code4fun.entity.dto.SubjectDTO;
import site.code4fun.util.StringUtils;

@Service
public class SubjectService extends BaseService{
	
	public List<SubjectDTO> getAll(){
		Organization org = getCurrentOrganization();
		if(null == org) return new ArrayList<>();
		List<Subject> lstSubject = subjectRepository.findByOrganizationId(org.getId());
		List<SubjectDTO> lstDTO  = new ArrayList<SubjectDTO>();
		lstSubject.forEach(_item -> {
			SubjectDTO dto = SubjectDTO.fromEntity(_item);
			dto.setOrganizationName(org.getName());
			lstDTO.add(dto);
		});
		return lstDTO;
	}
	
	public Subject insert(Subject s) throws Exception {
		if(StringUtils.isNull(s.getName())) throw new Exception("Tên môn học không được bỏ trống!");
		Organization org = getCurrentOrganization();
		if(null == org) throw new Exception("Chưa tạo trường!");
		String status = Status.ACTIVE.equalsIgnoreCase(s.getStatus()) ? s.getStatus() : Status.DRAFT;
		s.setOrganizationId(org.getId());
		s.setStatus(status);
		s.setCreatedBy(getCurrentId());
		s.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		return subjectRepository.save(s);
	}
	
	public boolean delete(Long id) throws Exception {
		Optional<Subject> item = subjectRepository.findById(id);
		if(!item.isPresent()) {
			subjectRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
