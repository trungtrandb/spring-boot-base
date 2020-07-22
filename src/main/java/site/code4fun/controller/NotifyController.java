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
import site.code4fun.service.NotifyService;
import site.code4fun.service.OrganizationService;

@RestController
@RequestMapping("/api/notify")
public class NotifyController {
	
	@Autowired
	NotifyService notifyService;
	
	@RequestMapping(path = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody Organization item){
		try {
			return ResponseEntity.ok(new Response(200, "Success", notifyService.create(item)));
		}catch (Exception e) {
			return ResponseEntity.ok(new Response(null, e.getMessage(), null));
		}
	}
}
