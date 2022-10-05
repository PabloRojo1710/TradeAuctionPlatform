package com.tcm.tradeauctionrest.application.exceptions;

public class NotPermissionException extends RuntimeException{

	private static final long serialVersionUID = -7615344173829379522L;

	public NotPermissionException(String username) {
        super("Profile " + username + " doesn't have permission to do that!");
    }
}
