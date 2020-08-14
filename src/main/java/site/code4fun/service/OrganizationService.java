package site.code4fun.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.entity.*;
import site.code4fun.entity.dto.UserDTO;
import site.code4fun.util.StringUtils;

@Service
public class OrganizationService extends BaseService{
	
	public boolean authorizeOrg(Long orgId, boolean required) {
		if(!required && orgId == null) {
			return true;
		}else {
			if(orgId == null) return false;
			Optional<Organization> org = organizationRepository.findById(orgId);
			return !org.isPresent() || !org.get().getUser().getId().equals(getCurrentId());
		}
	}

	
	
	public Organization getByUser(){
		return getCurrentOrganization();
	}
	
	public Organization create(Organization item) throws Exception {
		if(StringUtils.isNull(item.getName())) throw new Exception("Tên trường không được bỏ trống");
		if(null != item.getUser() && !item.getUser().getId().equals(getCurrentId()))
			throw new Exception("Item not found!");
		
		Optional<User> user = userRepository.findById(getCurrentId());
		item.setUser(user.get());
		item.setCreatedBy(user.get().getId());
		return organizationRepository.saveAndFlush(item);
	}
	
	public boolean deleteById(Long id){
		organizationRepository.deleteById(id);
		return true;
	}

	public List<User> getLstTeacherOfOrg() {
		Long id = getCurrentOrganization().getId();
		return jUserOrganizationRepository.getTeachersByOrgId(id);
	}

	public boolean deleteTeacher(Long teacherId) throws Exception {
		Long orgId = getCurrentOrganization().getId();
		List<Classes> lstTeacherClass = classRepository.findByOwnerId(teacherId);
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		List<Lession> lstTeacherLession = lessionRepository.findByClassIds(classIds);
		for (Lession ls : lstTeacherLession){
			if (Objects.equals(ls.getUserId(), teacherId)) throw new Exception("Không thể xóa giáo viên đang dạy buổi học " + ls.getTitle());
		}

		for (Classes _cl : lstTeacherClass){
			if (classIds.contains(_cl.getId())) throw new Exception("Không thể xóa giáo viên chủ nhiệm lớp " + _cl.getName());
		}

		userOrganizationRepository.deleteTeacherOrg(teacherId, orgId);
		return true;
	}

	public List<UserDTO> getLstParentOfOrg(){
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		return jStudentRepository.findParentByClassIds(classIds);
	}
}
