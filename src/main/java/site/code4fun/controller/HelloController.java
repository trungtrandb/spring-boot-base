package site.code4fun.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
@Slf4j
public class HelloController {

    @RequestMapping
    public String helloWorld(HttpServletRequest request){
        log.info(request.toString());
        return "Hello World";
    }
}
