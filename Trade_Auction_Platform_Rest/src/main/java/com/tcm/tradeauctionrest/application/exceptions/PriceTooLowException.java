package com.tcm.tradeauctionrest.application.exceptions;

public class PriceTooLowException extends RuntimeException{

	private static final long serialVersionUID = -3909948868315036009L;

	public PriceTooLowException(float initialPrice) {
        super("The price must be higher than" + initialPrice);
    }
}
