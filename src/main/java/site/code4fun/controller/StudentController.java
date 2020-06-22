package site.code4fun.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.Response;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	
	@Autowired
	StudentService studentService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<?> getAll(@RequestParam(name = "class", required = false) Long classId,
			@RequestParam(name = "organiztion", required = false) Long organizationId,
			@RequestParam(name = "group", required = false) Long groupId){
		try {
			return ResponseEntity.ok(new Response(200, "Successfull!", studentService.getAll(classId, groupId, organizationId)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody StudentDTO s){
		try {
			return ResponseEntity.ok(new Response(200, "Successfull!", studentService.create(s)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/get/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, "Successfull!", studentService.getById(id)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	} 
	
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@Valid @RequestBody StudentDTO s){
		try {
			return ResponseEntity.ok(new Response(200, "Successfull!", studentService.update(s)));
		} catch (Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ResponseEntity<?> update(@RequestParam("student")Long studentId,
			@RequestParam(value = "class", required = false) Long classId){
		try {
			return ResponseEntity.ok(new Response(200, "Successfull!", studentService.delete(studentId, classId)));
		} catch (Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}
