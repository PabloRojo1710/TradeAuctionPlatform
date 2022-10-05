package com.tcm.tradeauctionrest.application.dao;

import java.util.List;

import com.tcm.tradeauctionrest.domain.Bid;

public interface BidDAO {
	public void saveBid(Bid newBid, String username, String auctionid);

	public List<Bid> getBidsByAuctionId(String auctionId);

	public void updateBid(Bid bid);

	public List<Bid> getAllUserBids(String username);
	
	public List<Bid> getAllBidsWon(String username);
}
