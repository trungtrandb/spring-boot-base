package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.constant.ResponseMessage;
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
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, organizationService.getByUser()));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody Organization item){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.ADD_SUCCESS, organizationService.create(item)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/get-teacher", method = RequestMethod.GET)
	public ResponseEntity<?> getTeacher(){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, organizationService.getLstTeacherOfOrg()));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/get-parent", method = RequestMethod.GET)
	public ResponseEntity<?> getParent(){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, organizationService.getLstParentOfOrg()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> insert(@PathVariable Long id){
		try {
			organizationService.deleteById(id);
			return ResponseEntity.ok(new Response(200, ResponseMessage.DELETE_SUCCESS, null));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/delete-teacher/{teacherId}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteTeacher(@PathVariable Long teacherId){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.DELETE_SUCCESS, organizationService.deleteTeacher(teacherId)));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
}
