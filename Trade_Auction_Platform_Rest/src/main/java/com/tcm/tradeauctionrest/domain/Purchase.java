package com.tcm.tradeauctionrest.domain;

import java.util.Date;
import java.util.UUID;

import com.tcm.tradeauctionrest.application.dto.PurchaseDTO;

public class Purchase {
	
	private String id;
	private Date date;
	private float numBitcoins;
	private float numEuros;
	private float comission;
	
	public Purchase() {}
	
	public Purchase(PurchaseDTO purchase) {
		this.id = purchase.getId();
		this.date = purchase.getDate();
		this.numBitcoins = purchase.getNumBitcoins();
		this.numEuros = purchase.getNumEuros();
		this.comission = purchase.getComission();
	}
	
	public Purchase(Date date, float numBitcoins, float numEuros, float comission) {
		this.id = UUID.randomUUID().toString();
		this.date = date;
		this.numBitcoins = numBitcoins;
		this.numEuros = numEuros;
		this.comission = comission;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getNumBitcoins() {
		return numBitcoins;
	}

	public void setNumBitcoins(float numBitcoins) {
		this.numBitcoins = numBitcoins;
	}

	public float getNumEuros() {
		return numEuros;
	}

	public void setNumEuros(float numEuros) {
		this.numEuros = numEuros;
	}

	public float getComission() {
		return comission;
	}

	public void setComission(float comission) {
		this.comission = comission;
	}

}
