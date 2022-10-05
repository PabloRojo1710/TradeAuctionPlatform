package com.tcm.tradeauctionrest.domain;

import com.tcm.tradeauctionrest.application.dto.ProfileDTO;
import com.tcm.tradeauctionrest.application.exceptions.NotPermissionException;

public class Bidder extends Profile {

	public Bidder(ProfileDTO profile) {
		super(profile);
	}

	@Override
	public void buy(float amount, float buyBitcoin) {
		throw new NotPermissionException(this.userName);
	}
}
