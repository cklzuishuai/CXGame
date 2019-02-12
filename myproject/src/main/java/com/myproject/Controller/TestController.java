package com.myproject.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {
	
	@RequestMapping("/mytest")
	@ResponseBody
	public String test(){
		String res = "{name:ckl,age:24}";
		return res;
	}
	
}
