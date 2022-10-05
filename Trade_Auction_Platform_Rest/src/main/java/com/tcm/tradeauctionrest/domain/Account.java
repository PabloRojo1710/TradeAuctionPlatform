package com.tcm.tradeauctionrest.domain;

import com.tcm.tradeauctionrest.application.dto.AccountDTO;

public class Account {

	private float euros;
	private float bitcoins;

	public Account() {}

	public Account(AccountDTO accountDTO) {
		this.euros = accountDTO.getEuros();
		this.bitcoins = accountDTO.getBitcoins();
	}

	public Account(float euros, float bitcoins) {
		this.euros = euros;
		this.bitcoins = bitcoins;
	}

	public float getEuros() {
		return euros;
	}

	public void setEuros(float euros) {
		this.euros = euros;
	}

	public float getBitcoins() {
		return bitcoins;
	}

	public void setBitcoins(float bitcoins) {
		this.bitcoins = bitcoins;
	}

	public void updateBitcoins(float bitcoins) {
		this.bitcoins += bitcoins;
	}

	public void updateEuros(float euros) {
		this.euros += euros;
	}

}
