package com.tcm.tradeauctionrest.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tcm.tradeauctionrest.application.dto.ComissionDTO;
import com.tcm.tradeauctionrest.utilities.Utilities;

@Repository
public class CommissionDAO implements com.tcm.tradeauctionrest.application.dao.CommissionDAO{

	private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<ComissionDTO> comissionRowMapper = (resultSet, i) -> {
		ComissionDTO comissionDTO = new ComissionDTO();

		comissionDTO.setType(resultSet.getString("type"));
		comissionDTO.setDateP(resultSet.getDate("datep"));
		comissionDTO.setEuros(resultSet.getFloat("euros"));
		comissionDTO.setComission(resultSet.getFloat("comission"));

		return comissionDTO;
	};
	
	
	
	public CommissionDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<ComissionDTO> getComissions() {
		final String query = "select 'Bitcoin Purchase' as type, p.datep, p.eurosperbitcoin as euros, p.comission "
				+ "from purchase p " + "UNION " + "select 'Bid Comission', b.datep, b.priceeuros, b.comission "
				+ "from bid b " + "where b.comission > 0";
		return jdbcTemplate.query(query, comissionRowMapper);
	}
	

	@Override
	public float getMoneyBlocked(String username) {
		final String query = "SELECT SUM(priceeuros*numberbitcoins*?) AS eurosBlocked " + "FROM bid "
				+ "WHERE profile_username = ? AND auction_id IN (SELECT id " + "FROM auction " + "WHERE closed = ?)";
		return Optional.ofNullable(this.jdbcTemplate.queryForObject(query, Float.class,(1+Utilities.COMISSION), username, "false")).orElse(0f);
	}

}
