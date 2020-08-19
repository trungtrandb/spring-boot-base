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

import site.code4fun.constant.ResponseMessage;
import site.code4fun.entity.Response;
import site.code4fun.entity.dto.ClassDTO;
import site.code4fun.entity.dto.PointDTO;
import site.code4fun.service.ClassService;

@Controller
@RequestMapping("/api/class")
public class ClassController {

	@Autowired
	ClassService classService;
	
	@RequestMapping(value = "/get-by-group", method = RequestMethod.GET)
	public ResponseEntity<?> getAll(@RequestParam(required = false) Long id,
									@RequestParam(required = false) String status){
		try {
			return new ResponseEntity<>(new Response(200, ResponseMessage.QUERY_SUCCESS, classService.getByGroupId(id, status)), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
	
	@PreAuthorize("@classService.authorizeClass(#c.groupClassId)")
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody ClassDTO c) {
		try {
			return new ResponseEntity<>(new Response(200, ResponseMessage.ADD_SUCCESS, classService.insert(c)), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, classService.getById(id)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			classService.delete(id);
			return new ResponseEntity<>(new Response(200, ResponseMessage.DELETE_SUCCESS, null), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}	
	
	@RequestMapping(value = "get-point", method = RequestMethod.GET)
	public ResponseEntity<?> getListPoint(@RequestParam(required = false) Long classId, 
			@RequestParam(required = false) Long subjectId, 
			@RequestParam(required = false) Byte sem){
		try {
			return new ResponseEntity<>(new Response(200, ResponseMessage.QUERY_SUCCESS, classService.getPoint(classId, subjectId, sem)), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "update-point", method = RequestMethod.POST)
	public ResponseEntity<?> updatePoint(@RequestBody PointDTO point){
		try {
			return new ResponseEntity<>(new Response(200, ResponseMessage.UPDATE_SUCCESS, classService.updatePoint(point)), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "export-point", method = RequestMethod.GET)
	public ResponseEntity<?> exportExcel(@RequestParam(required = false) Long classId,
										 @RequestParam(required = false) Long subjectId,
										 @RequestParam(required = false) Byte sem,
										 @RequestParam(required = false) Byte numOfTest){
		try {
			return new ResponseEntity<>(new Response(200, ResponseMessage.QUERY_SUCCESS, classService.exportPointClass(classId, subjectId, sem, numOfTest)), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "get-list-teacher/{classId}", method = RequestMethod.GET)
	public ResponseEntity<?> updatePoint(@PathVariable Long classId){
		try {
			return new ResponseEntity<>(new Response(200, ResponseMessage.QUERY_SUCCESS, classService.getListTeacher(classId)), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Response(500, e.getMessage(), null), HttpStatus.OK);
		}
	}
}
