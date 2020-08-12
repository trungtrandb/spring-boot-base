package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import site.code4fun.entity.Response;
import site.code4fun.entity.dto.ClassDTO;
import site.code4fun.service.ClassService;

@Controller
@RequestMapping("/api/class")
public class ClassController {

	@Autowired
	ClassService classService;
	
	@RequestMapping(value = "/get-by-group", method = RequestMethod.GET)
	public ResponseEntity<?> getAll(@RequestParam(required = false) Long id){
		try {
			return new ResponseEntity<>(new Response(200, "Successfull!!!", classService.getByGroupId(id)), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
	
	@PreAuthorize("@classService.authorizeClass(#c.groupClassId)")
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody ClassDTO c) {
		try {
			return new ResponseEntity<>(new Response(200, "Thêm mới thành công", classService.insert(c)), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, "Success", classService.getById(id)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			classService.delete(id);
			return new ResponseEntity<>(new Response(200, "Xóa thành công", null), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}	
}
