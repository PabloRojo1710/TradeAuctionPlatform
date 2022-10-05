package com.tcm.tradeauctionrest.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcm.tradeauctionrest.application.AuctionBidController;
import com.tcm.tradeauctionrest.application.dto.AuctionDTO;
import com.tcm.tradeauctionrest.application.dto.BidDTO;

@RestController
@CrossOrigin
@Validated
public class AuctionBidRestController {

	private AuctionBidController auctionBidController;

	public AuctionBidRestController(AuctionBidController controller) {
		this.auctionBidController = controller;
	}

	@PostMapping("/profiles/{username}/auctions")
	public void createAuction(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username,
			@RequestBody @Valid AuctionDTO auction) {
		this.auctionBidController.createAuction(auction, username);
	}

	@GetMapping("/profiles/{username}/auctions")
	public List<AuctionDTO> listProfileAuctions(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username) {
		return this.auctionBidController.listProfileAuctions(username);
	}

	@GetMapping("/auctions")
	public List<AuctionDTO> listAllActiveAuctions() {
		return this.auctionBidController.listAllActiveAuctions();
	}

	@PostMapping("/profiles/{username}/auctions/{auctionid}/bids")
	public void createBid(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username,
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String auctionid,
			@RequestBody @Valid String bidInformation) {
		JSONObject json = new JSONObject(bidInformation);
		float bitcoins = json.getFloat("bitcoins");
		float pricePerBitcoin = json.getFloat("euros");

		this.auctionBidController.createBid(username, auctionid, bitcoins, pricePerBitcoin);
	}

	@GetMapping("/profiles/{username}/bids")
	public List<BidDTO> getAllUserBids(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username) {
		return this.auctionBidController.getAllUserBids(username);
	}

	@GetMapping("/auctions/{auctionId}/bids")
	public List<BidDTO> getAllAuctionBids(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String auctionId) {
		return this.auctionBidController.getAllAuctionBids(auctionId);
	}

	@GetMapping("/profiles/{username}/bids/won")
	public List<BidDTO> getAllBidsWon(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username) {
		return this.auctionBidController.getAllBidsWon(username);
	}

	@GetMapping("/profiles/{username}/auctions/bids")
	public List<AuctionDTO> getAllAuctionsAndBids(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username) {
		return this.auctionBidController.getAllAuctionsAndBids(username);
	}

}
