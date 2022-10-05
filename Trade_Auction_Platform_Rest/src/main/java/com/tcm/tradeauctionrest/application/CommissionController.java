package com.tcm.tradeauctionrest.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tcm.tradeauctionrest.application.dao.CommissionDAO;
import com.tcm.tradeauctionrest.application.dto.ComissionDTO;

@Service
public class CommissionController {

	private CommissionDAO commissionDAO;
	
	public CommissionController(CommissionDAO commissionDAO) {
		this.commissionDAO = commissionDAO;
	}

	public List<ComissionDTO> getComissions() {
		return commissionDAO.getComissions();
	}
	
	public float getMoneyBlocked(String username) {
		return this.commissionDAO.getMoneyBlocked(username);
	}
	
}
