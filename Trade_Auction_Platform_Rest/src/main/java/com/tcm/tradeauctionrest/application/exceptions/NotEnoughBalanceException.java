package com.tcm.tradeauctionrest.application.exceptions;

public class NotEnoughBalanceException extends RuntimeException{
	private static final long serialVersionUID = 8241323863146397096L;

	public NotEnoughBalanceException(String username) {
        super("Profile " + username + " doesn't have enough balance!");
    }
}
