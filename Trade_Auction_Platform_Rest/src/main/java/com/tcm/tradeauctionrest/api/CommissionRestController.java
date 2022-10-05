package com.tcm.tradeauctionrest.api;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tcm.tradeauctionrest.application.CommissionController;
import com.tcm.tradeauctionrest.application.dto.ComissionDTO;
import com.tcm.tradeauctionrest.utilities.Utilities;

@RestController
@CrossOrigin
@Validated
public class CommissionRestController {

	private CommissionController commissionController;

	public CommissionRestController(CommissionController commissionController) {
		this.commissionController = commissionController;
	}

	@GetMapping("/profiles/{username}/euros/blocked")
	public float getMoneyBlocked(
			@PathVariable @NotNull(message = "Can't be null") @NotBlank(message = "Can't be blank") String username) {
		return this.commissionController.getMoneyBlocked(username);
	}

	@GetMapping("/comissions")
	public List<ComissionDTO> getComissions() {
		return commissionController.getComissions();
	}

	@GetMapping("/platform-comission")
	public float getPlatformComission() {
		return Utilities.COMISSION;
	}

}
