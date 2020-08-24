package site.code4fun.service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.entity.*;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.entity.dto.UserDTO;
import site.code4fun.util.StringUtils;

@Service
public class OrganizationService extends BaseService{
	
	public boolean authorizeOrg(Long orgId) {
		if(orgId == null) return true;
		Organization org = getCurrentOrganization();
		return org.getUser().getId().equals(getCurrentId());
	}

	
	
	public Organization getByUser(){
		return getCurrentOrganization();
	}
	
	public Organization create(Organization item) throws Exception {
		if(StringUtils.isNull(item.getName())) throw new Exception("Tên trường không được bỏ trống");
		if(!StringUtils.isNull(item.getPhone()) && !Pattern.matches("^(09|012|08|016|03|07|08|05|02)\\d{8,}", item.getPhone())) throw new Exception("Số điện thoại không đúng định dạng!");
		Optional<User> user = userRepository.findById(getCurrentId());
		user.ifPresent(item::setUser);
		user.ifPresent(value -> item.setCreatedBy(value.getId()));
		return organizationRepository.saveAndFlush(item);
	}
	
	public boolean deleteById(Long id){
		organizationRepository.deleteById(id);
		return true;
	}

	public List<User> getLstTeacherOfOrg() {
		Organization org = getCurrentOrganization();
		return null != org ? jUserOrganizationRepository.getTeachersByOrgId(org.getId()) : new ArrayList<>();
	}

	public boolean deleteTeacher(Long teacherId) throws Exception {
		List<Classes> lstTeacherClass = classRepository.findByOwnerId(teacherId);
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		List<Lession> lstTeacherLession = lessionRepository.findByClassIds(classIds);
		for (Lession ls : lstTeacherLession){
			if (Objects.equals(ls.getUserId(), teacherId)) throw new Exception("Không thể xóa giáo viên đang dạy buổi học " + ls.getTitle());
		}

		for (Classes _cl : lstTeacherClass){
			if (classIds.contains(_cl.getId())) throw new Exception("Không thể xóa giáo viên chủ nhiệm lớp " + _cl.getName());
		}

		Organization org = getCurrentOrganization();
		if(null != org){
			userOrganizationRepository.deleteTeacherOrg(teacherId, org.getId());
			return true;
		}
		return false;
	}

	public List<UserDTO> getLstParentOfOrg(){
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		return jStudentRepository.findParentByClassIds(classIds);
	}

    public Object getReportOverview() {
    	Map<String, Integer> mapRes = new HashMap<>();
    	List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		mapRes.put("totalClass", classIds.size());

    	List<StudentDTO> lstStudent = jStudentRepository.findByClassId(classIds, null);
		mapRes.put("totalStudent", lstStudent.size());

		Organization org = getCurrentOrganization();
		List<User> lstTeacher = org != null ? jUserOrganizationRepository.getTeachersByOrgId(org.getId()) : new ArrayList<>();
		mapRes.put("totalTeacher", lstTeacher.size());

		List<UserDTO> lstParent = jStudentRepository.findParentByClassIds(classIds);
		List<UserDTO> lstParentActive = lstParent.stream().filter(userDTO -> "ACTIVE".equalsIgnoreCase(userDTO.getStatus())).collect(Collectors.toList());
		mapRes.put("totalParentActive", lstParentActive.size());

    	return mapRes;
	}
}
