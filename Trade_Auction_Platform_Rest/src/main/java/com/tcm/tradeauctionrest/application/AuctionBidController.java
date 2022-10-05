package com.tcm.tradeauctionrest.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tcm.tradeauctionrest.application.dao.BidDAO;
import com.tcm.tradeauctionrest.application.dao.ProfileDAO;
import com.tcm.tradeauctionrest.application.dto.AuctionDTO;
import com.tcm.tradeauctionrest.application.dto.BidDTO;
import com.tcm.tradeauctionrest.domain.Auction;
import com.tcm.tradeauctionrest.domain.Bid;
import com.tcm.tradeauctionrest.domain.Profile;
import com.tcm.tradeauctionrest.persistence.AuctionDAO;
import com.tcm.tradeauctionrest.utilities.Utilities;

@Service
@EnableScheduling
public class AuctionBidController {
	private ProfileDAO profileDAO;
	private AuctionDAO auctionDAO;
	private BidDAO bidDAO;

	public AuctionBidController(ProfileDAO profileDAO, AuctionDAO auctionDAO, BidDAO bidDAO) {
		this.profileDAO = profileDAO;
		this.auctionDAO = auctionDAO;
		this.bidDAO = bidDAO;
	}

	@Scheduled(cron = "*/60 * * * * *")
	public void checkFinishedAuctions() {
		List<Auction> auctionsToClose = auctionDAO.getAllToCloseAuctions();
		for (Auction auction : auctionsToClose) {
			List<Bid> allBids = bidDAO.getBidsByAuctionId(auction.getId());
			
			List<Bid> bids = filterGoodBids(allBids, auction.getBitcoins());
			dealMoney(bids, auction);
			
			List<Bid> noAcceptedBids = filterBadBids(allBids, auction.getBitcoins());
			proccesNotAcceptedBids(noAcceptedBids);
			
			auction.setClosed(true);
			auctionDAO.updateAuction(auction);
		}
	}
	
	private List<Bid> filterGoodBids(List<Bid> bids, float auctionBitcoins) {
		ArrayList<Bid> retBids = new ArrayList<Bid>();
		float bitcoinsSum = 0;
		for (Bid bid : bids) {
			bitcoinsSum += bid.getNumberBitcoins();
			retBids.add(bid);
			if (bitcoinsSum >= auctionBitcoins) 
				return retBids;	
		}
		return retBids;
	}
	
	private List<Bid> filterBadBids(List<Bid> allBids, float auctionBitcoins) {
		ArrayList<Bid> retBids = new ArrayList<Bid>();
		float bitcoinsSum = 0;
		boolean isLastChecked = false;
		for (Bid bid : allBids) {
			bitcoinsSum += bid.getNumberBitcoins();
			if (bitcoinsSum >= auctionBitcoins && !isLastChecked) 
				isLastChecked = true;
			if (isLastChecked) 
				retBids.add(bid);	
		}
		return retBids;
	}
	
	private void dealMoney(List<Bid> bids, Auction auction) {
		float numberBitcoinsGiven = 0.0f;
		float totalEurosEarned = 0;
		for (int i = 0; i < bids.size(); i++) {
			if (i == bids.size() - 1) {
				totalEurosEarned += processFinalBid(bids.get(i), auction.getBitcoins() - numberBitcoinsGiven,
						auction.getId());
			} else {
				totalEurosEarned += giveBitcoins(bids.get(i));
				numberBitcoinsGiven += bids.get(i).getNumberBitcoins();
			}
		}
		Profile profile = profileDAO.getProfile(profileDAO.getUsernameByAuctionId(auction.getId()));
		profile.getAccount().updateEuros(totalEurosEarned);
		profileDAO.updateProfileBalance(profile);
	}
	
	private float processFinalBid(Bid bid, float bitcoinsLeft, String auctionId) {
		if (bitcoinsLeft >= bid.getNumberBitcoins()) 
			processFinalBidNoOverflow(bid, bitcoinsLeft, auctionId);
		else 
			processFinalBidOverflow(bid, bitcoinsLeft, auctionId);

		return bid.getBitcoinsEarned() * bid.getPriceEuros();
	}

	private void processFinalBidOverflow(Bid bid, float bitcoinsLeft, String auctionId) {
		Profile profile = profileDAO.getProfile(bid.getUserName());
		profile.incrementBitcoins(bitcoinsLeft);
		profile.getAccount().updateEuros((bid.getNumberBitcoins() - bitcoinsLeft) * bid.getPriceEuros());
		profileDAO.updateProfileBalance(profile);

		bid.setBitcoinsEarned(bitcoinsLeft);
		bid.setComission(bid.getBitcoinsEarned() * bid.getPriceEuros() * Utilities.COMISSION);
		bidDAO.updateBid(bid);
	}

	private void processFinalBidNoOverflow(Bid bid, float bitcoinsLeft, String auctionId) {
		Profile profile = profileDAO.getProfile(bid.getUserName());
		profile.incrementBitcoins(bid.getNumberBitcoins());
		profileDAO.updateProfileBalance(profile);

		bid.setBitcoinsEarned(bid.getNumberBitcoins());
		bid.setComission(bid.getBitcoinsEarned() * bid.getPriceEuros() * Utilities.COMISSION);
		bidDAO.updateBid(bid);

		Profile profileBroker = profileDAO.getProfile(profileDAO.getUsernameByAuctionId(auctionId));
		profileBroker.getAccount().updateBitcoins(bitcoinsLeft - bid.getBitcoinsEarned());
		profileDAO.updateProfileBalance(profileBroker);	
	}
	
	private float giveBitcoins(Bid bid) {
		Profile profile = profileDAO.getProfile(bid.getUserName());
		profile.incrementBitcoins(bid.getNumberBitcoins());
		profileDAO.updateProfileBalance(profile);

		bid.setBitcoinsEarned(bid.getNumberBitcoins());
		bid.setComission(bid.getBitcoinsEarned() * bid.getPriceEuros() * Utilities.COMISSION);
		bidDAO.updateBid(bid);

		return bid.getPriceEuros() * bid.getBitcoinsEarned();
	}
	
	private void proccesNotAcceptedBids(List<Bid> noAcceptedBids) {
		for (Bid bid : noAcceptedBids) {
			Profile profile = profileDAO.getProfile(bid.getUserName());
			profile.getAccount().updateEuros(bid.getNumberBitcoins() * bid.getPriceEuros());
			profileDAO.updateProfileBalance(profile);
		}
	}

	public void createAuction(AuctionDTO auction, String username) {
		Profile profile = profileDAO.getProfile(username);
		profile.tryToCreateAuction(auction.getBitcoins());
		this.auctionDAO.createAuction(new Auction(auction), username);
		this.profileDAO.updateProfileBalance(profile);
	}

	public List<AuctionDTO> listProfileAuctions(String username) {
		return this.auctionDAO.listProfileAuctions(username).stream().map(AuctionDTO::new).collect(Collectors.toList());
	}

	public void createBid(String username, String auctionid, float bitcoins, float pricePerBitcoin) {
		Profile profile = profileDAO.getProfile(username);
		Auction auction = auctionDAO.getAuction(auctionid);
		profile.tryToCreateBid(auction.getInitialPrice(), pricePerBitcoin, bitcoins);
		Bid newBid = new Bid(new Date(), pricePerBitcoin, bitcoins, 0, 0, username);
		bidDAO.saveBid(newBid, username, auctionid);
		profileDAO.updateProfileBalance(profile);
	}

	public List<AuctionDTO> listAllActiveAuctions() {

		return auctionDAO.getAllActiveAuctions().stream().map(AuctionDTO::new).collect(Collectors.toList());
	}

	public List<BidDTO> getAllUserBids(String username) {
		return bidDAO.getAllUserBids(username).stream().map(BidDTO::new).collect(Collectors.toList());
	}

	public List<BidDTO> getAllAuctionBids(String auctionId) {

		return bidDAO.getBidsByAuctionId(auctionId).stream().map(BidDTO::new).collect(Collectors.toList());
	}

	public List<BidDTO> getAllBidsWon(String username) {
		return bidDAO.getAllBidsWon(username).stream().map(BidDTO::new).collect(Collectors.toList());
	}

	public List<AuctionDTO> getAllAuctionsAndBids(String username) {
		return this.auctionDAO.getAllAuctionsAndBids(username).stream().map(AuctionDTO::new)
				.collect(Collectors.toList());
	}

	
}
