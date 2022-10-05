package com.tcm.tradeauctionrest.application.dao;

import java.util.List;

import com.tcm.tradeauctionrest.domain.Auction;

public interface AuctionDAO {
	public void createAuction(Auction auction, String username);

	public List<Auction> listProfileAuctions(String username);
	
	public List<Auction> getAllActiveAuctions();

	public List<Auction> getAllToCloseAuctions();

	public void updateAuction(Auction auction);
	
	public Auction getAuction(String auctionid);
	
	public List<Auction> getAllAuctionsAndBids(String username);
}
