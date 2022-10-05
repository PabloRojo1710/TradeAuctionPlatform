package com.tcm.tradeauctionrest.domain;

import com.tcm.tradeauctionrest.application.dto.ProfileDTO;
import com.tcm.tradeauctionrest.application.exceptions.NotEnoughBalanceException;

public class Broker extends Profile {

	public Broker(ProfileDTO profile) {
		super(profile);
	}

	public float howManyBitcoins() {
		return this.account.getBitcoins();
	}

	public void buy(float amount, float bitcoinPrice) {
		if (canBuy(amount, bitcoinPrice))
			updateAcountBalance(amount, bitcoinPrice);
		else
			throw new NotEnoughBalanceException(this.userName);

	}

	private boolean canBuy(float amount, float bitcoinPrice) {
		return (amount * bitcoinPrice) <= this.account.getEuros();
	}
}
