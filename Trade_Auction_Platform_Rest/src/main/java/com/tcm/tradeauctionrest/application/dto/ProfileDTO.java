package com.tcm.tradeauctionrest.application.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.tcm.tradeauctionrest.domain.Profile;

public class ProfileDTO {

	@NotNull(message = "Can't be null")
	@NotBlank(message = "Can't be blank")
	@Size(min=3, message = "Too short: minimum length 3")
	protected String userName;
	
	@NotNull(message = "Can't be null")
	@NotBlank(message = "Can't be blank")
	@Size(min=3, message = "Too short: minimum length 3")
	protected String password;
	
	@NotNull(message = "Can't be null")
	@NotBlank(message = "Can't be blank")
	@Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b", message = "Email must look like an email: xxx@xxx.xxx")
	protected String email;
	
	protected AccountDTO account;
	
	@NotNull(message = "Can't be null")
	protected ProfileType type;

	protected boolean enabled;

	public ProfileDTO() {
	}

	public ProfileDTO(Profile profile) {
		this.userName = profile.getUserName();
		this.password = profile.getPassword();
		this.email = profile.getEmail();
		this.account = new AccountDTO(profile.getAccount());
		this.type = Enum.valueOf(ProfileType.class, profile.getClass().getSimpleName().toUpperCase());
		this.enabled = profile.isEnabled();
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

	public AccountDTO getAccount() {
		return account;
	}

	public void setAccount(AccountDTO account) {
		this.account = account;
	}

	public ProfileType getType() {
		return type;
	}

	public void setType(ProfileType type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
