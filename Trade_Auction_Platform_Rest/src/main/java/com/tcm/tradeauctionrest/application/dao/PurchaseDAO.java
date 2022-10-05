package com.tcm.tradeauctionrest.application.dao;

import java.util.List;

import com.tcm.tradeauctionrest.domain.Purchase;

public interface PurchaseDAO {

	public void savePurchase(Purchase purchase, String username);

	public List<Purchase> getProfilePurchases(String username);
	
}
