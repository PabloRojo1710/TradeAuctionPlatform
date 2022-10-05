package com.tcm.tradeauctionrest.domain;

import java.util.Date;
import java.util.UUID;

import com.tcm.tradeauctionrest.application.dto.BidDTO;

public class Bid {
	private String bidId;
	private Date dateP;
	private float priceEuros;
	private float numberBitcoins;
	private float bitcoinsEarned;
	private float comission;
	private String userName;
	
	public Bid() {}
	
	public Bid(Date dateP, float priceEuros, float numberBitcoins, float bitcoinsEarned,
			float comission, String userName) {
		
		this.bidId = UUID.randomUUID().toString();
		this.dateP = dateP;
		this.priceEuros = priceEuros;
		this.numberBitcoins = numberBitcoins;
		this.bitcoinsEarned = bitcoinsEarned;
		this.comission = comission;
		this.userName = userName;
	}
	
	public Bid (BidDTO bidDTO) {
		this.bidId = UUID.randomUUID().toString();
		this.dateP = bidDTO.getDateP();
		this.priceEuros = bidDTO.getPriceEuros();
		this.numberBitcoins = bidDTO.getNumberBitcoins();
		this.bitcoinsEarned = bidDTO.getBitcoinsEarned();
		this.comission = bidDTO.getComission();
		this.userName = bidDTO.getUserName();
	}

	public String getBidId() {
		return bidId;
	}

	public void setBidId(String bidId) {
		this.bidId = bidId;
	}

	public Date getDateP() {
		return dateP;
	}

	public void setDateP(Date dateP) {
		this.dateP = dateP;
	}

	public float getPriceEuros() {
		return priceEuros;
	}

	public void setPriceEuros(float priceEuros) {
		this.priceEuros = priceEuros;
	}

	public float getNumberBitcoins() {
		return numberBitcoins;
	}

	public void setNumberBitcoins(float numberBitcoins) {
		this.numberBitcoins = numberBitcoins;
	}

	public float getBitcoinsEarned() {
		return bitcoinsEarned;
	}

	public void setBitcoinsEarned(float bitcoinsEarned) {
		this.bitcoinsEarned = bitcoinsEarned;
	}

	public float getComission() {
		return comission;
	}

	public void setComission(float comission) {
		this.comission = comission;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
		
}
