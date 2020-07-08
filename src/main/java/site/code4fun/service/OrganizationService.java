package site.code4fun.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.entity.Organization;
import site.code4fun.entity.User;
import site.code4fun.repository.OrganizationRepository;
import site.code4fun.repository.UserRepository;
import site.code4fun.repository.jdbc.JUserOrganizationRepository;

@Service
public class OrganizationService extends BaseService{
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JUserOrganizationRepository jUserOrganizationRepository;
	
	public Organization getById(Long id) throws Exception {
		Optional<Organization> item = organizationRepository.findById(id);
		if(!item.isPresent() || item.get().getUser().getId() != getCurrentId()) 
			throw new Exception("Organization not found!");
		
		return item.get();
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
		if(item.get().getUser().getId() != getCurrentId()) throw new Exception("Can't delete by not owner!");
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

}
