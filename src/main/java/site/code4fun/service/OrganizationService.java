package site.code4fun.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.Organization;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.UserDTO;

@Service
public class OrganizationService extends BaseService{
	
	public boolean authorizeOrg(Long orgId, boolean required) {
		if(!required && orgId == null) {
			return true;
		}else {
			if(orgId == null) return false;
			Optional<Organization> org = organizationRepository.findById(orgId);
			if(!org.isPresent() || org.get().getUser().getId() != getCurrentId()) return true;
		}	
		return false;
	}

	public Organization getById(Long id) throws Exception {
		return organizationRepository.findById(id).get();
	}
	
	public Organization getByUser() throws Exception {
		return getCurrentOrganization();
	}
	
	public Organization create(Organization item) throws Exception {
		if(null != item.getUser() && item.getUser().getId() != getCurrentId())
			throw new Exception("Item not found!");
		
		Optional<User> user = userRepository.findById(getCurrentId());
		item.setUser(user.get());
		item.setCreatedBy(user.get().getId());
		return organizationRepository.saveAndFlush(item);
	}
	
	public boolean deleteById(Long id) throws Exception {
		organizationRepository.deleteById(id);
		return true;
	}

	public List<User> getLstTeacherOfOrg() {
		Long id = getCurrentOrganization().getId();
		return jUserOrganizationRepository.getTeachersByOrgId(id);
	}

	public boolean deleteTeacher(Long teacherId) throws Exception {
		Long orgId = getCurrentOrganization().getId();
		userOrganizationRepository.deleteTeacherOrg(teacherId, orgId);
		return true;
	}

	public List<UserDTO> getLstParentOfOrg() throws Exception {
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		return jStudentRepository.findParentByClassIds(classIds);
	}

}
