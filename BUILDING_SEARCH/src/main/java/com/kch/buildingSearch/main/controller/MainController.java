package com.kch.buildingSearch.main.controller;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import com.kch.buildingSearch.main.model.dto.VerificationResult;
import com.kch.buildingSearch.main.model.service.MainService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final MainService service;
	
    @RequestMapping("")
    public String mainPage() {
        return "common/main";
    }

 // 중개업자 인증 api 보내기
    @GetMapping("test")
    public String apiTest(Model model,
        @RequestParam("regNum") String inputRegNum,
        @RequestParam("dealerName") String inputDealerName
    ) {
    	
    	String message;
    	String path;
        List<VerificationResult> verificationResult = new ArrayList<>();
        int result = service.verifiDealer(verificationResult, inputRegNum, inputDealerName);
        
        if(result < 1) {
        	message = "결과가 없습니다.";
        	path = "common/fail";
        } else {
        	message = "결과가 있습니다.";
        	path = "common/result";
        }
        
        
        return path;
    }



}