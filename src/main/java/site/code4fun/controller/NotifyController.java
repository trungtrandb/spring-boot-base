package site.code4fun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.constant.ResponseMessage;
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
			return ResponseEntity.ok(new Response(200, "Thành công", notifyService.create(item)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<?> getAllNotify(){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, notifyService.getAll()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/is-read", method = RequestMethod.POST) // Đánh dấu notify đã đọc
	public ResponseEntity<?> updateIsRead(@RequestBody(required = false) List<Long> ids){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.UPDATE_SUCCESS, notifyService.isRead(ids)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/count-notify", method = RequestMethod.GET) // Tổng số notify, notify isRead
	public ResponseEntity<?> countNotify(){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, notifyService.countNotify()));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, notifyService.getById(id)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/get-by-user", method = RequestMethod.GET)
	public ResponseEntity<?> getByUser(@RequestParam(required = false) Long page,@RequestParam(required = false) Integer size){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, notifyService.getByUser(page, size)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteNotify(@PathVariable Long id){
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.DELETE_SUCCESS, notifyService.delete(id)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}
