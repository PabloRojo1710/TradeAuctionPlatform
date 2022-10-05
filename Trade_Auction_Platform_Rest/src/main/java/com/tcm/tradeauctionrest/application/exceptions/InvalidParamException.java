package com.tcm.tradeauctionrest.application.exceptions;

public class InvalidParamException extends RuntimeException {

	private static final long serialVersionUID = -4226094103591614536L;

	public InvalidParamException(String param, String cause) {
        super(param + ": " + cause);
    }
}
