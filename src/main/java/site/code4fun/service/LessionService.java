package site.code4fun.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
		List<Long> idsClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		
		if(StringUtils.isNull(c.getTitle())) throw new Exception("Tên bài học không được bỏ trống!");
		if(!idsClass.contains(c.getClassId())) throw new Exception("Lớp học không được bỏ trống!");
		if(null == c.getSubjectId()) throw new Exception("Môn học không được bỏ trống!");
		if(null == c.getUserId()) throw new Exception("Giáo viên giảng dạy không được bỏ trống!");
		
		
		return lessionRepository.saveAndFlush(c);
	}

	public boolean delete(Long id) {
		lessionRepository.deleteById(id);
		return true;
	}
	public Optional<Lession> getById(Long id){
		return lessionRepository.findById(id);
	}
}
