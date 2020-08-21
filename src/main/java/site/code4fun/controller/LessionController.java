package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import site.code4fun.constant.ResponseMessage;
import site.code4fun.entity.Lession;
import site.code4fun.entity.Response;
import site.code4fun.service.LessionService;
import site.code4fun.util.StringUtils;

import java.util.Date;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/api/lession")
public class LessionController {
//
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		dateFormat.setLenient(false);
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
//	}

	@Autowired
	LessionService lessionService;

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<?> getAll(@RequestParam(value = "classId", required = false) Long classId,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "subjectId", required = false) Long subjectId,
			@RequestParam(value = "name", required = false) String name) {
		try {
			Date startDate = null;
			if (!StringUtils.isNull(startTime)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				startDate = (Date) dateFormat.parse(startTime);
			}
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, lessionService.getAll(classId, startDate, subjectId, name)));
		} catch (Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}

	@RequestMapping(value = "/get-by-class", method = RequestMethod.GET)
	public ResponseEntity<?> getByClass(@RequestParam(value = "id") Long id) {
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, lessionService.getByClassId(id)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insert(@RequestBody Lession s) {
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.ADD_SUCCESS, lessionService.insert(s)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}

	@RequestMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.DELETE_SUCCESS, lessionService.delete(id)));
		} catch (Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}

	@RequestMapping(value = "/get/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(new Response(200, ResponseMessage.QUERY_SUCCESS, lessionService.getById(id)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}
