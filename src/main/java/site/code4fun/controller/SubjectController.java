package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import site.code4fun.Response;
import site.code4fun.entity.Subject;
import site.code4fun.service.SubjectService;

@Controller
@RequestMapping("/api/subject")
public class SubjectController {

	@Autowired
	SubjectService subjectService;
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<?> getAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit){
		try {
			return ResponseEntity.ok(new Response(200, "success", subjectService.getAll(page, limit)));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody Subject s) {
		try {
			return ResponseEntity.ok(new Response(200, "Successful", subjectService.insert(s)));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(new Response(200, "Successful", subjectService.delete(id)));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	
}
