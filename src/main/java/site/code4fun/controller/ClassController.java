package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import site.code4fun.Response;
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
			return new ResponseEntity<>(new Response(null, "success", classService.getByGroupId(id)), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody ClassDTO c) {
		try {
			return new ResponseEntity<>(new Response(null, "success", classService.insert(c)), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			classService.delete(id);
			return new ResponseEntity<>(new Response(null, "success", null), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new Response(null, e.getMessage(), null), HttpStatus.OK);
		}
	}	
}
