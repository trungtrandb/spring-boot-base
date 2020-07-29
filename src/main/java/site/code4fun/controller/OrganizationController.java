package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.entity.Organization;
import site.code4fun.entity.Response;
import site.code4fun.service.OrganizationService;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
	
	@Autowired
	OrganizationService organizationService;
	
	
	@RequestMapping(path = "/get-by-user", method = RequestMethod.GET)
	public ResponseEntity<?> getByUserId(){
		try {
			return ResponseEntity.ok(new Response(200, "Success", organizationService.getByUser()));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@PreAuthorize("@organizationService.authorizeOrg(#id, true)")
	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, "Success", organizationService.getById(id)));
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
	
	@PreAuthorize("@organizationService.authorizeOrg(#id, false)")
	@RequestMapping(path = "/get-teacher", method = RequestMethod.GET)
	public ResponseEntity<?> getTeacher(){
		try {
			return ResponseEntity.ok(new Response(200, "Success", organizationService.getLstTeacherOfOrg()));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@PreAuthorize("@organizationService.authorizeOrg(#id, false)")
	@RequestMapping(path = "/get-parent", method = RequestMethod.GET)
	public ResponseEntity<?> getParent(){
		try {
			return ResponseEntity.ok(new Response(200, "Success", organizationService.getLstParentOfOrg()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@PreAuthorize("@organizationService.authorizeOrg(#id, true)")
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> insert(@PathVariable Long id){
		try {
			organizationService.deleteById(id);
			return ResponseEntity.ok(new Response(200, "Success", null));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@PreAuthorize("@organizationService.authorizeOrg(#orgId, true)")
	@RequestMapping(path = "/delete-teacher", method = RequestMethod.GET)
	public ResponseEntity<?> deleteTeacher(@RequestParam(value = "teacher_id") Long teacherId){
		try {
			organizationService.deleteTeacher(teacherId);
			return ResponseEntity.ok(new Response(200, "Success", null));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}

}
