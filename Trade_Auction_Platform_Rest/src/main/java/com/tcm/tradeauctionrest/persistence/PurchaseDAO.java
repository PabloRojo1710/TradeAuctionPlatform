package com.tcm.tradeauctionrest.persistence;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tcm.tradeauctionrest.domain.Purchase;

@Repository
public class PurchaseDAO implements com.tcm.tradeauctionrest.application.dao.PurchaseDAO{

	private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<Purchase> purchaseRowMapper = (resultSet, i) -> {
		Purchase purchase = new Purchase();

		purchase.setId(resultSet.getString("id"));
		purchase.setDate(resultSet.getDate("datep"));
		purchase.setNumBitcoins(resultSet.getFloat("numBitcoins"));
		purchase.setNumEuros(resultSet.getFloat("eurosperbitcoin"));
		purchase.setComission(resultSet.getFloat("comission"));

		return purchase;
	};
	
	public PurchaseDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void savePurchase(Purchase purchase, String username) {
		final String insert = "INSERT INTO purchase (profile_username, id, numBitcoins, eurosperbitcoin, comission, datep) VALUES (?,?,?,?,?,?)";
		jdbcTemplate.update(insert, username, purchase.getId(), purchase.getNumBitcoins(), purchase.getNumEuros(),
				purchase.getComission(), new java.sql.Date(purchase.getDate().getTime()));
	}

	@Override
	public List<Purchase> getProfilePurchases(String username) {
		final String query = "SELECT id, numBitcoins, eurosperbitcoin, comission, datep " + "FROM purchase "
				+ "WHERE profile_username=? " + "ORDER BY datep desc";
		return this.jdbcTemplate.query(query, purchaseRowMapper, username);

	}


}
