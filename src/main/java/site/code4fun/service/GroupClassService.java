package site.code4fun.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Organization;
import site.code4fun.entity.dto.GroupClassDTO;
import site.code4fun.repository.GroupClassRepository;
import site.code4fun.repository.OrganizationRepository;

@Service
public class GroupClassService extends BaseService{

	@Autowired
	GroupClassRepository groupClassRepository;
	
	@Autowired
	OrganizationRepository organizationRepository;
	
	public GroupClass create(GroupClassDTO item) throws Exception {
		if(null == item.getOrganizationId()) throw new Exception("Select 1 Organization!");
		Optional<Organization> organization = organizationRepository.findById(item.getOrganizationId());
		if(!organization.isPresent()) throw new Exception("Organization not found!!!");
		GroupClass gc = GroupClass.builder()
				.name(item.getName())
				.organization(organization.get())
				.build();
		return groupClassRepository.saveAndFlush(gc);
	}
	
	public List<GroupClass> getByOrganizationId(Long organizationId) throws Exception {
		List<Long> idsOrganization = new ArrayList<Long>();
		if(null == organizationId) {
			List<Organization> lstOrganization =  organizationRepository.findByUserId(getCurrentId());
			idsOrganization = lstOrganization.stream().map(Organization::getId).collect(Collectors.toList());
		}else {
			idsOrganization = Arrays.asList(organizationId);
		}
		
		return idsOrganization.size() > 0 ? groupClassRepository.findByOrganizationIds(idsOrganization) : new ArrayList<>();
	}
	
	public GroupClass getById(Long id) throws Exception {
		Optional<GroupClass> item = groupClassRepository.findById(id);
		if(!item.isPresent()) throw new Exception("Item not found!");
		return item.get();
	}
	
	public boolean deleteById(Long id) throws Exception {
		Optional<GroupClass> gc = groupClassRepository.findById(id);
		if(!gc.isPresent()) throw new Exception("Item not found!!");
		
		Optional<Organization> item = organizationRepository.findById(gc.get().getOrganization().getId());
		if(item.get().getUser().getId() != getCurrentId()) throw new Exception("Can't delete "+ item.get().getName() +" by not owner!");
		groupClassRepository.deleteById(id);
		return true;
	}
	
	
}
