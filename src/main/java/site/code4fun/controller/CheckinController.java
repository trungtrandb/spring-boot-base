package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import site.code4fun.entity.Checkin;
import site.code4fun.entity.Response;
import site.code4fun.entity.dto.CheckinFilterDTO;
import site.code4fun.service.CheckinService;

@Controller
@RequestMapping("/api/checkin")
public class CheckinController {

	@Autowired
	CheckinService checkinService;
	
	@RequestMapping(value = "/getAll")
	public ResponseEntity<?> getAll(@RequestBody CheckinFilterDTO filter){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", checkinService.getAll(filter)));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody Checkin s) {
		try {
			return ResponseEntity.ok(new Response(200, "Successful", checkinService.insert(s)));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}	
}
