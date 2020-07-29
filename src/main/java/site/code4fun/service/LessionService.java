package site.code4fun.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.Lession;
import site.code4fun.util.StringUtils;

@Service
public class LessionService extends BaseService{
	
	public List<Lession> getAll(){
		List<Classes> lstClass = getCurrentClasses();
		List<Long> idsClass = lstClass.stream().map(Classes::getId).collect(Collectors.toList());
		
		return jLessionRepository.findByClassIds(idsClass);
	}
	
	public List<Lession> getByClassId(Long id){
		return jLessionRepository.findByClassIds(Arrays.asList(id));
	}
	
	public Lession insert(Lession c) throws Exception {
		if(StringUtils.isNull(c.getTitle())) throw new Exception("Lession name is null!!");
		List<Long> idsClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		
		if(!idsClass.contains(c.getClassId())) throw new Exception("Class not found!!");
		
		return lessionRepository.saveAndFlush(c);
	}

	public boolean delete(Long id) {
		lessionRepository.deleteById(id);
		return true;
	}
}
