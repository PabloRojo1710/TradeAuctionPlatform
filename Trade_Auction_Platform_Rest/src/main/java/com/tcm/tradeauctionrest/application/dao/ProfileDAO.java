package com.tcm.tradeauctionrest.application.dao;

import com.tcm.tradeauctionrest.domain.Profile;

public interface ProfileDAO {

	public Profile createProfile(Profile profile);

	public Profile getProfile(String userName);

	public void updateProfileBalance(Profile profile);
	
	public String getUsernameByAuctionId(String id);
	

}
