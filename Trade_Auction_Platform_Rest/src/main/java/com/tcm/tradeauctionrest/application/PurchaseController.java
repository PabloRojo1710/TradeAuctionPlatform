package com.tcm.tradeauctionrest.application;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tcm.tradeauctionrest.application.dao.ProfileDAO;
import com.tcm.tradeauctionrest.application.dao.PurchaseDAO;
import com.tcm.tradeauctionrest.application.dto.PurchaseDTO;
import com.tcm.tradeauctionrest.domain.Purchase;

import com.tcm.tradeauctionrest.utilities.Utilities;

@Service
public class PurchaseController {

	private PurchaseDAO purchaseDAO;
	private ProfileDAO profileDAO;
	private ProfilesController profilesController;
	private BitcoinExternalAPIController bitcoinController;

	public PurchaseController(PurchaseDAO purchaseDAO, ProfileDAO profileDAO,
			BitcoinExternalAPIController bitcoinController, ProfilesController profilesController) {
		this.purchaseDAO = purchaseDAO;
		this.profileDAO = profileDAO;
		this.bitcoinController = bitcoinController;
		this.profilesController = profilesController;
	}

	public void buyBitcoin(String username, float amount) {
		var profile = Utilities.profileDTO2Profile(profilesController.getProfile(username));

		float bitcoinPrice = bitcoinController.queryBitcoinPrice();

		profile.buy(amount, this.bitcoinController.buyBitcoin(amount));

		profileDAO.updateProfileBalance(profile);
		Purchase purchase = new Purchase(new Date(), amount, bitcoinPrice * amount,
				bitcoinPrice * amount * Utilities.COMISSION);
		purchaseDAO.savePurchase(purchase, username);
	}

	public List<PurchaseDTO> listUserPurchases(String username) {
		return this.purchaseDAO.getProfilePurchases(username).stream().map(PurchaseDTO::new)
				.collect(Collectors.toList());
	}

}
