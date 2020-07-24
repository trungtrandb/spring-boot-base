package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import site.code4fun.entity.Response;
import site.code4fun.entity.dto.NotifyDTO;
import site.code4fun.service.NotifyService;

@RestController
@RequestMapping("/api/notify")
public class NotifyController {
	
	@Autowired
	NotifyService notifyService;
	
	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody NotifyDTO item){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.create(item)));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
}
