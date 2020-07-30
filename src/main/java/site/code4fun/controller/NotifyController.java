package site.code4fun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.entity.Notify;
import site.code4fun.entity.Response;
import site.code4fun.service.NotifyService;

@RestController
@RequestMapping("/api/notify")
public class NotifyController {
	
	@Autowired
	NotifyService notifyService;
	
	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody Notify item){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.create(item)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<?> getAllNotify(){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.getAll()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/is-read", method = RequestMethod.POST) // Đánh dấu notify đã đọc
	public ResponseEntity<?> updateIsRead(@RequestBody(required = false) List<Long> ids){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.isRead(ids)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.getById(id)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/getByUser", method = RequestMethod.GET)
	public ResponseEntity<?> getByUser(){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.getByUser()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteNotify(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.delete(id)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}
