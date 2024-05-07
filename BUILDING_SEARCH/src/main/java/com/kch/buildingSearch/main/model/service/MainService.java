package com.kch.buildingSearch.main.model.service;

import java.util.List;

import org.springframework.ui.Model;

import com.kch.buildingSearch.main.model.dto.VerificationResult;

public interface MainService {

	int verifiDealer(List<VerificationResult> verificationResult, String inputRegNum, String inputDealerName);

}
