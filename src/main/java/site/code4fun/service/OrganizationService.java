package site.code4fun.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.entity.Organization;
import site.code4fun.entity.User;
import site.code4fun.repository.OrganizationRepository;
import site.code4fun.repository.UserRepository;

@Service
public class OrganizationService extends BaseService{
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public List<Organization> getByUserId(Long id) throws Exception {
		List<Organization> lstItem = organizationRepository.findByUserId(id);
		if(lstItem.size() == 0) throw new Exception("Organization not found!");
		return lstItem;
	}
	
	public List<Organization> getByUser() throws Exception {
		List<Organization> lstItem = organizationRepository.findByUserId(getCurrentId());
		if(lstItem.size() == 0) throw new Exception("Organization not found!");
		return lstItem;
	}
	
	public Organization create(Organization item) throws Exception {
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

}
