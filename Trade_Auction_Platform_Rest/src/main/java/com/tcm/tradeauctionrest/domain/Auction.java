package com.tcm.tradeauctionrest.domain;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.tcm.tradeauctionrest.application.dto.AuctionDTO;

public class Auction {
	private String id;
	private float bitcoins;
	private Date startingDate;
	private Date endingDate;
	private float initialPrice;
	private boolean closed;
	private List<Bid> bids;

	public Auction() {}
	
	public Auction(float bitcoins, Date startingDate, Date endingDate, float initialPrice, boolean isClosed,
			List<Bid> bids) {

		this.id = UUID.randomUUID().toString();
		this.bitcoins = bitcoins;
		this.startingDate = startingDate;
		this.endingDate = endingDate;
		this.initialPrice = initialPrice;
		this.closed = isClosed;
		this.bids = bids;
	}

	public Auction(AuctionDTO auctionDTO) {
		this.id = UUID.randomUUID().toString();
		this.bitcoins = auctionDTO.getBitcoins();
		this.startingDate = auctionDTO.getStartingDate();
		this.endingDate = auctionDTO.getEndingDate();
		this.initialPrice = auctionDTO.getInitialPrice();
		this.closed = auctionDTO.isClosed();
		this.bids = auctionDTO.getBids().stream().map(bidDTO -> new Bid(bidDTO)).collect(Collectors.toList());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getBitcoins() {
		return bitcoins;
	}

	public void setBitcoins(float bitcoins) {
		this.bitcoins = bitcoins;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

	public float getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(float initialPrice) {
		this.initialPrice = initialPrice;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean isClosed) {
		this.closed = isClosed;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

}
