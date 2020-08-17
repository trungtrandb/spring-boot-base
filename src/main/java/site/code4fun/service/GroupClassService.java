package site.code4fun.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Organization;
import site.code4fun.entity.dto.GroupClassDTO;

@Service
public class GroupClassService extends BaseService{
	
	public GroupClass create(GroupClassDTO item) throws Exception {
		Organization org = getCurrentOrganization();
		if(null == org) throw new Exception("Chưa tạo trường!");
		GroupClass gc = GroupClass.builder()
				.id(item.getId())
				.name(item.getName())
				.organization(org)
				.build();
		return groupClassRepository.saveAndFlush(gc);
	}
	
	public List<GroupClass> getAll() throws Exception {		
		return groupClassRepository.findByOrganizationId(getCurrentOrganization().getId());
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
