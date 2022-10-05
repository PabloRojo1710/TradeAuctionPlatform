package com.tcm.tradeauctionrest.persistence;

import com.tcm.tradeauctionrest.application.dto.AccountDTO;
import com.tcm.tradeauctionrest.application.dto.ProfileDTO;
import com.tcm.tradeauctionrest.application.dto.ProfileType;
import com.tcm.tradeauctionrest.domain.Profile;
import com.tcm.tradeauctionrest.utilities.Utilities;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileDAO implements com.tcm.tradeauctionrest.application.dao.ProfileDAO {

	private JdbcTemplate jdbcTemplate;

	private final RowMapper<ProfileDTO> profileRowMapper = (resultSet, i) -> {
		ProfileDTO profile = new ProfileDTO();

		profile.setUserName(resultSet.getString("username"));
		profile.setEmail(resultSet.getString("email"));
		profile.setPassword(resultSet.getString("password"));
		profile.setType(Enum.valueOf(ProfileType.class, resultSet.getString("type")));
		profile.setAccount(new AccountDTO(resultSet.getFloat("euros"), resultSet.getFloat("bitcoins")));
		profile.setEnabled(Boolean.parseBoolean(resultSet.getString("enabled")));

		return profile;
	};

	public ProfileDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

	}

	@Override
	public Profile createProfile(Profile profile) {
		String sql = "INSERT INTO profile (username, password, email, type) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, profile.getUserName(), profile.getPassword(), profile.getEmail(),
				profile.getClass().getSimpleName().toUpperCase());

		sql = "INSERT INTO profile_account (profile_username, euros, bitcoins) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, profile.getUserName(), 0, 0);

		sql = "INSERT INTO authorities (username, role) VALUES (?, ?)";
		jdbcTemplate.update(sql, profile.getUserName(), "ROLE_" + profile.getClass().getSimpleName().toUpperCase());

		return profile;
	}

	@Override
	public Profile getProfile(String userName) {
		final String query = "SELECT username, password, email, type, enabled, euros, bitcoins FROM profile JOIN profile_account ON username = profile_username WHERE username = ?";
		ProfileDTO profileDTO = jdbcTemplate.queryForObject(query, profileRowMapper, userName);
		return Utilities.profileDTO2Profile(profileDTO);
	}

	@Override
	public void updateProfileBalance(Profile profile) {
		final String query = "UPDATE profile_account SET euros = ?, bitcoins = ? WHERE profile_username = ?";
		jdbcTemplate.update(query, profile.getAccount().getEuros(), profile.getAccount().getBitcoins(),
				profile.getUserName());
	}

	@Override
	public String getUsernameByAuctionId(String id) {
		final String query = "SELECT profile_username FROM auction WHERE id = ? ";
		return this.jdbcTemplate.queryForObject(query, String.class, id);
	}

}
