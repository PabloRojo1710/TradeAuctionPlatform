package com.tcm.tradeauctionrest.api;

import java.security.Principal;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcm.tradeauctionrest.application.ProfilesController;
import com.tcm.tradeauctionrest.application.dto.ProfileDTO;

@RestController
@CrossOrigin
@Validated
public class ProfilesRestController {

	private ProfilesController profileController;

	public ProfilesRestController(ProfilesController profileController) {
		this.profileController = profileController;
	}

	@PostMapping("/profiles")
	public ProfileDTO createProfile(@RequestBody @Valid ProfileDTO profile) {
		return profileController.createProfile(profile);
	}

	@GetMapping("/profiles/me")
	public ProfileDTO getMyProfile(Principal principal) {
		return profileController.getProfile(principal.getName());
	}

	@PostMapping("/profiles/{userName}/euros/{amount}")
	public void depositMoney(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String userName,
			@PathVariable @NotNull(message = "Can't be null") @Min(value = 0, message = "Can't be negative") float amount) {
		this.profileController.depositMoney(userName, amount);
	}

}
