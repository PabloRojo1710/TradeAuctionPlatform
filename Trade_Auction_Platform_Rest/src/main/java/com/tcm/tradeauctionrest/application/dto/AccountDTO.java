package com.tcm.tradeauctionrest.application.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.tcm.tradeauctionrest.domain.Account;

public class AccountDTO {

	@NotNull(message = "Can't be null")
	@Min(value = 0, message = "Can't be negative")
	private float euros;
	@NotNull(message = "Can't be null")
	@Min(value = 0, message = "Can't be negative")
    private float bitcoins;
    
    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.euros = account.getEuros();
        this.bitcoins = account.getBitcoins();
    }

    public AccountDTO(float euros, float bitcoins) {
        this.euros = euros;
        this.bitcoins = bitcoins;
    }

    public float getEuros() {
        return euros;
    }

    public void setEuros(float euros) {
        this.euros = euros;
    }

    public float getBitcoins() {
        return bitcoins;
    }

    public void setBitcoins(float bitcoins) {
        this.bitcoins = bitcoins;
    }
}
