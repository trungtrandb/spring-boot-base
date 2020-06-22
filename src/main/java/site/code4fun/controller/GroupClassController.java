package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.Response;
import site.code4fun.entity.dto.GroupClassDTO;
import site.code4fun.service.GroupClassService;

@RestController
@RequestMapping("/api/group-class")
public class GroupClassController {
	
	@Autowired
	GroupClassService groupClassService;

	@RequestMapping(path = "/get-by-organization", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@RequestParam(required = false) Long id){
		try {
			return ResponseEntity.ok(new Response(200, "success", groupClassService.getByOrganizationId(id)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody GroupClassDTO item){
		try {
			return ResponseEntity.ok(new Response(200, "success", groupClassService.create(item)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> insert(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, "success", groupClassService.deleteById(id)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}
