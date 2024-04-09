package com.kh.test.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.test.customer.model.dto.Customer;
import com.kh.test.customer.model.service.CustomerService;

@Controller
@RequestMapping("customer")
public class CustomerController {

	@Autowired
	private CustomerService service;
	
	@RequestMapping("regist")
	public String regist (
		@RequestParam("name") String name,
		@RequestParam("tel") String tel,
		@RequestParam("address") String address,
		Model model
		) {
		
		int result = service.regist(name, tel, address);
		
		String message = null;
		String path = "/result";
		
		model.addAttribute("name", name);
		
		return path;
	}
}
