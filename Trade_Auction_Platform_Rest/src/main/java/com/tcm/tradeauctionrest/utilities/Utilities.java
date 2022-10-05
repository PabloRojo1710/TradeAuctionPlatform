package com.tcm.tradeauctionrest.utilities;

import com.tcm.tradeauctionrest.application.dto.ProfileDTO;
import com.tcm.tradeauctionrest.domain.Admin;
import com.tcm.tradeauctionrest.domain.Bidder;
import com.tcm.tradeauctionrest.domain.Broker;
import com.tcm.tradeauctionrest.domain.Profile;

public class Utilities {

	public static final float COMISSION = 0.001f;

	public static Profile profileDTO2Profile(ProfileDTO profileDTO) {
		switch (profileDTO.getType()) {
			case ADMIN:
				return new Admin(profileDTO);
			case BROKER:
				return new Broker(profileDTO);
			case BIDDER:
				return new Bidder(profileDTO);
			default:
				throw new IllegalStateException();
		}
	}
}
