package com.tcm.tradeauctionrest.application.dto;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tcm.tradeauctionrest.application.exceptions.InvalidParamException;
import com.tcm.tradeauctionrest.domain.Auction;

public class AuctionDTO {
	
	private String id;
	
	@NotNull(message = "Can't be null")
	@Min(value = 0, message = "Can't be negative")
	private float bitcoins;
	
	@NotNull(message = "Can't be null")
	private Date startingDate;
	
	@NotNull(message = "Can't be null")
	private Date endingDate;
	
	@NotNull(message = "Can't be null")
	@Min(value = 0, message = "Can't be negative")
	private float initialPrice;
	
	private boolean closed;
	
	private List<BidDTO> bids;

	public AuctionDTO() {
	}

	public AuctionDTO(String id, float bitcoins, Date startingDate, Date endingDate, float initialPrice, boolean closed,
			List<BidDTO> bids) {

		this.id = id;
		this.bitcoins = bitcoins;
		this.startingDate = startingDate;
		this.endingDate = endingDate;
		this.initialPrice = initialPrice;
		this.closed = closed;
		this.bids = bids;
	}

	public AuctionDTO(Auction auction) {
		this.id = auction.getId();
		this.bitcoins = auction.getBitcoins();
		this.startingDate = auction.getStartingDate();
		this.endingDate = auction.getEndingDate();
		this.initialPrice = auction.getInitialPrice();
		this.closed = auction.isClosed();
		this.bids = auction.getBids().stream().map(bid -> new BidDTO(bid)).collect(Collectors.toList());
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
        if (endingDate.before(startingDate))
            throw new InvalidParamException("Starting date", "After ending date");
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

    public Date getEndingDate() {
        if (endingDate.before(startingDate))
            throw new InvalidParamException("Ending date", "Before starting date");
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

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public List<BidDTO> getBids() {
		return bids;
	}

	public void setBids(List<BidDTO> bids) {
		this.bids = bids;
	}

}
