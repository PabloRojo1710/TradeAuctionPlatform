package com.tcm.tradeauctionrest.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcm.tradeauctionrest.application.dao.ProfileDAO;
import com.tcm.tradeauctionrest.application.dto.AccountDTO;
import com.tcm.tradeauctionrest.application.dto.ProfileDTO;
import com.tcm.tradeauctionrest.domain.Profile;
import com.tcm.tradeauctionrest.utilities.Utilities;

@Service
public class ProfilesController {
	
	private PasswordEncoder passwordEncoder;
	private ProfileDAO profileDAO;
		
	
	public ProfilesController(PasswordEncoder passwordEncoder, ProfileDAO profileDAO) {
		this.passwordEncoder = passwordEncoder;
		this.profileDAO = profileDAO;
	}

	public ProfileDTO createProfile(ProfileDTO profileDTO) {
		profileDTO.setAccount(new AccountDTO(0, 0));
		Profile profile = Utilities.profileDTO2Profile(profileDTO);
		profile.setPassword(passwordEncoder.encode(profile.getPassword()));
		return new ProfileDTO(profileDAO.createProfile(profile));
	}

	public ProfileDTO getProfile(String userName) {
		return new ProfileDTO(profileDAO.getProfile(userName));
	}
	
	public void depositMoney(String userName, float amount) {
		Profile profile = profileDAO.getProfile(userName);
		profile.depositMoney(amount);
		profileDAO.updateProfileBalance(profile);
	}
}
