package site.code4fun.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
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
	
	public List<Organization> getByUser() throws Exception {
		List<Organization> lstItem = organizationRepository.findByUserId(getCurrentId());
		if(lstItem.size() == 0) throw new Exception("Organization not found!");
		return lstItem;
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
		Optional<Organization> item = organizationRepository.findById(id);
		if(!item.isPresent()) throw new Exception("Item not found!");
		if(item.get().getUser().getId() != getCurrentId()) throw new Exception("Không có quyền xóa!");
		organizationRepository.deleteById(id);
		return true;
	}

	public List<User> getLstTeacherOfOrg(Long id) {
		List<Long> ids = getCurrentOrganization().stream().map(Organization::getId).collect(Collectors.toList());
		if(null != id) {
			ids = Arrays.asList(id);
		}
		return jUserOrganizationRepository.getTeachersByOrgIds(ids);
	}

	public boolean deleteTeacher(Long teacherId, Long orgId) throws Exception {
		Optional<Organization> item = organizationRepository.findById(orgId);
		if(!item.isPresent()) throw new Exception("Item not found!");
		if(item.get().getUser().getId() != getCurrentId()) throw new Exception("Không có quyền xóa!");
		userOrganizationRepository.deleteTeacherOrg(teacherId, orgId);
		return true;
	}

	public List<UserDTO> getLstParentOfOrg(Long orgId) throws Exception {
		List<Long> classIds = new ArrayList<>();
		if(orgId != null) {
			Optional<Organization> org = organizationRepository.findById(orgId);
			if(!org.isPresent() || org.get().getUser().getId() != getCurrentId()) throw new Exception("Không tìm thấy hoặc không có quyền xem!");
			List<Long> groupClassIds = groupClassRepository.findByOrganizationIds(Arrays.asList(org.get().getId())).stream().map(GroupClass::getId).collect(Collectors.toList());
			if(groupClassIds.size() == 0 )return new ArrayList<>();
			classIds = classRepository.findByGroupId(groupClassIds).stream().map(Classes::getId).collect(Collectors.toList());
		}else {
			classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		}
		return jStudentRepository.findParentByClassIds(classIds);
	}

}
