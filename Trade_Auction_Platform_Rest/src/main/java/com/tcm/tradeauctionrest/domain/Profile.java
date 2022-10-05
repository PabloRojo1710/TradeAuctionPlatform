package com.tcm.tradeauctionrest.domain;

import com.tcm.tradeauctionrest.application.dto.ProfileDTO;
import com.tcm.tradeauctionrest.application.exceptions.NotEnoughBalanceException;
import com.tcm.tradeauctionrest.application.exceptions.PriceTooLowException;
import com.tcm.tradeauctionrest.utilities.Utilities;

public abstract class Profile {

	protected String userName;
	protected String password;
	protected String email;
	protected Account account;

	protected boolean enabled;

	public Profile(String userName, String password, String email, Account account, boolean enabled) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.account = account;
		this.enabled = enabled;
	}

	public Profile(ProfileDTO profileDTO) {
		this.account = new Account(profileDTO.getAccount());
		this.userName = profileDTO.getUserName();
		this.email = profileDTO.getEmail();
		this.password = profileDTO.getPassword();
		this.enabled = profileDTO.isEnabled();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	protected void updateAcountBalance(float amount, float bitcoinPrice) {
		this.account.setBitcoins(this.account.getBitcoins() + amount);
		float bitcoinPriceComission = bitcoinPrice + (bitcoinPrice * Utilities.COMISSION);
		this.account.setEuros(this.account.getEuros() - (amount * bitcoinPriceComission));
	}

	public void decrementBitcoins(float bitcoins) {
		this.account.setBitcoins(account.getBitcoins() - bitcoins);
	}

	public void decrementEuros(float euros) {
		this.account.setEuros(account.getEuros() - euros);
	}

	public abstract void buy(float amount, float buyBitcoin);

	public boolean hasEnoughBitcoins(float bitcoins) {
		return this.account.getBitcoins() >= bitcoins;
	}

	public void depositMoney(float amount) {
		this.account.updateEuros(amount);
	}

	public boolean hasEnoughEuros(float euros) {
		return this.account.getEuros() >= euros;
	}

	public void incrementBitcoins(float numberBitcoins) {
		this.account.setBitcoins(this.account.getBitcoins() + numberBitcoins);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void tryToCreateAuction(float bitcoins) {
		if(this.hasEnoughBitcoins(bitcoins)) {
			this.decrementBitcoins(bitcoins);
		}
		else
			throw new NotEnoughBalanceException(this.userName);
	}
	
	public void tryToCreateBid(float initialPrice, float pricePerBitcoin, float bitcoins) {
		if(initialPrice <= pricePerBitcoin) {
			if(this.hasEnoughEuros(pricePerBitcoin*bitcoins + pricePerBitcoin*bitcoins*Utilities.COMISSION)) {
				this.decrementEuros(pricePerBitcoin*bitcoins + pricePerBitcoin*bitcoins*Utilities.COMISSION);
			}
			else
				throw new NotEnoughBalanceException(this.userName);
		}
		else
			throw new PriceTooLowException(initialPrice);
	}
	
}
