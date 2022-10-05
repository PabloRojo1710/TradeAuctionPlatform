package com.tcm.tradeauctionrest.application.dto;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tcm.tradeauctionrest.domain.Bid;

public class BidDTO {
	
	private String bidId;
	
	@NotNull(message = "Can't be null")
	private Date dateP;
	
	@NotNull(message = "Can't be null")
	@Min(value = 0, message = "Can't be negative")
	private float priceEuros;
	
	@NotNull(message = "Can't be null")
	@Min(value = 0, message = "Can't be negative")
	private float numberBitcoins;
	
	private float bitcoinsEarned;
	
	private float comission;
	
	@NotNull(message = "Can't be null")
	private String userName;
	
	public BidDTO() {}
	
	public BidDTO(Bid bid) {
		this.bidId = bid.getBidId();
		this.dateP = bid.getDateP();
		this.priceEuros = bid.getPriceEuros();
		this.numberBitcoins = bid.getNumberBitcoins();
		this.bitcoinsEarned = bid.getBitcoinsEarned();
		this.comission = bid.getComission();
		this.userName = bid.getUserName();
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
