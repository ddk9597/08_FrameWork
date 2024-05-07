package com.kch.buildingSearch.main.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.kch.buildingSearch.main.model.dto.VerificationResult;
import com.kch.buildingSearch.main.model.mapper.MainMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

	public final MainMapper mapper;
	
	
	@Override
	public int verifiDealer(List<VerificationResult> verificationResult, String inputRegNum, String inputDealerName) {
		
		return mapper.verifiDealer();
	}
	
}
