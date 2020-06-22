package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.Response;
import site.code4fun.entity.Organization;
import site.code4fun.service.OrganizationService;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
	
	@Autowired
	OrganizationService organizationService;
	
	
	@RequestMapping("/get-by-user")
	public ResponseEntity<?> getByUserId(){
		try {
			return ResponseEntity.ok(new Response(200, "Success", organizationService.getByUser()));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody Organization item){
		try {
			return ResponseEntity.ok(new Response(200, "Success", organizationService.create(item)));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/delete/{id}")
	public ResponseEntity<?> insert(@PathVariable Long id){
		try {
			organizationService.deleteById(id);
			return ResponseEntity.ok(new Response(200, "Success", null));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}

}
