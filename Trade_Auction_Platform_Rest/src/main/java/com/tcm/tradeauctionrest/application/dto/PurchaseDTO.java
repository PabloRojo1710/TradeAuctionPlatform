package com.tcm.tradeauctionrest.application.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tcm.tradeauctionrest.domain.Purchase;

public class PurchaseDTO {
	
	private String id;
	private Date date;
	private float numBitcoins;
	private float numEuros;
	private float comission;
	
	public PurchaseDTO() {}
	
	public PurchaseDTO(Purchase purchase) {
		this.id = purchase.getId();
		this.date = purchase.getDate();
		this.numBitcoins = purchase.getNumBitcoins();
		this.numEuros = purchase.getNumEuros();
		this.comission = purchase.getComission();
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
	
	public void setUnparsedDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH24:MI:ss");
		try {
			this.date = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
