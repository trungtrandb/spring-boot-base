package site.code4fun.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import site.code4fun.constant.ResponseMessage;
import site.code4fun.entity.Response;
import site.code4fun.entity.Student;
import site.code4fun.entity.User;
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
			return ResponseEntity.ok(new Response(200, ResponseMessage.ADD_SUCCESS, studentService.getAll(classId, groupId, organizationId)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody StudentDTO s){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.ADD_SUCCESS, studentService.create(s)));
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
	
	@RequestMapping(value = "/get-student-by-id")
	public ResponseEntity<?> getStudentById(@RequestParam(required = false) Long id){
		try {
			return ResponseEntity.ok(new Response(200, "Successfull!", studentService.getStudentById(id)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	} 
	
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@Valid @RequestBody StudentDTO s){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.UPDATE_SUCCESS, studentService.update(s)));
		} catch (Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/update-student", method = RequestMethod.POST )
	public ResponseEntity<?> updateStudent(@RequestBody Student student){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", studentService.updateStudent(student)));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ResponseEntity<?> update(@RequestParam("student")Long studentId,
			@RequestParam(value = "class", required = false) Long classId){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.DELETE_SUCCESS, studentService.delete(studentId, classId)));
		} catch (Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	
	@RequestMapping(value = "/upload-image", method = RequestMethod.POST)
	public ResponseEntity<?> saveFile(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		String rootPath = servletContext.getRealPath("");
		String imagePath = "/resources/image/" + file.getOriginalFilename();
		Path path = Paths.get(rootPath + imagePath);
		try {
			Files.write(path, file.getBytes(), StandardOpenOption.CREATE);
			return ResponseEntity.ok(new Response(200, "Success", imagePath));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}
