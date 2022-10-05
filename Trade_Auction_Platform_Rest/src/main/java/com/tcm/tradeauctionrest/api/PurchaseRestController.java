package com.tcm.tradeauctionrest.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcm.tradeauctionrest.application.BitcoinExternalAPIController;
import com.tcm.tradeauctionrest.application.PurchaseController;
import com.tcm.tradeauctionrest.application.dto.PurchaseDTO;
import com.tcm.tradeauctionrest.utilities.Utilities;

@RestController
@CrossOrigin
@Validated
public class PurchaseRestController {
	private PurchaseController purchaseController;
	private BitcoinExternalAPIController bitcoinController;

	public PurchaseRestController(PurchaseController purchaseController,
			BitcoinExternalAPIController bitcoinController) {
		this.purchaseController = purchaseController;
		this.bitcoinController = bitcoinController;
	}

	@GetMapping("/bitcoins/price")
	public Map<String, Object> queryBitcoinPrice() {
		float bitcoinPrice = this.bitcoinController.queryBitcoinPrice();
		float bitcoinPriceComission = bitcoinPrice + (bitcoinPrice * Utilities.COMISSION);
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 8705602468833026974L;
			{
				put("bitcoinPrice", bitcoinPrice);
				put("comission", Utilities.COMISSION);
				put("bitcoinPriceComission", bitcoinPriceComission);
			}
		};
	}

	@PostMapping("/profiles/{username}/bitcoins/{amount}")
	public void buyBitcoin(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username,
			@PathVariable @NotNull(message = "Can't be null") @Min(value = 0, message = "Can't be negative") float amount) {
		this.purchaseController.buyBitcoin(username, amount);
	}

	@GetMapping("/profiles/{username}/purchases")
	public List<PurchaseDTO> listPurcharses(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username) {
		return this.purchaseController.listUserPurchases(username);
	}
}
