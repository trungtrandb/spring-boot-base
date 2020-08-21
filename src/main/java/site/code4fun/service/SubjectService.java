package site.code4fun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Lession;
import site.code4fun.entity.Organization;
import site.code4fun.entity.Subject;
import site.code4fun.util.StringUtils;

@Service
public class SubjectService extends BaseService{
	
	public List<Subject> getAll(String status){
		Organization org = getCurrentOrganization();
		List<Subject> lstSubject = null != org ? subjectRepository.findByOrganizationId(org.getId()) : new ArrayList<>();
		if (!StringUtils.isNull(status)){
			lstSubject.removeIf(_x -> !_x.getStatus().equalsIgnoreCase(status));
		}
		return lstSubject;
	}
	
	public List<Subject> getSubjectBySchoolId(Long id){
		return subjectRepository.findByOrganizationId(id);
	}


	public Subject insert(Subject s) throws Exception {
		if(StringUtils.isNull(s.getName())) throw new Exception("Tên môn học không được bỏ trống!");
		Organization org = getCurrentOrganization();
		if(null == org) throw new Exception("Chưa tạo trường!");
		String status = Status.ACTIVE.equalsIgnoreCase(s.getStatus()) ? s.getStatus() : Status.DRAFT;
		s.setOrganizationId(org.getId());
		s.setStatus(status);
		return subjectRepository.save(s);
	}
	
	public boolean delete(Long id) throws Exception {
		Optional<Subject> item = subjectRepository.findById(id);
		if(item.isPresent()) {
			List<Lession> lstLession = lessionRepository.findBySubjectId(id);
			if(lstLession.size() > 0 ) throw new Exception("Không thể xóa môn học đã có lịch học");
			subjectRepository.deleteById(id);
			return true;
		}
		return false;
	}
	public Optional<Subject> getById(Long id){
		return subjectRepository.findById(id);
	}
}
