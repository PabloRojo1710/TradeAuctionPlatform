package com.tcm.tradeauctionrest.application.dao;

import java.util.List;

import com.tcm.tradeauctionrest.application.dto.ComissionDTO;

public interface CommissionDAO {
	
	public List<ComissionDTO> getComissions();

	public float getMoneyBlocked(String username);
}
