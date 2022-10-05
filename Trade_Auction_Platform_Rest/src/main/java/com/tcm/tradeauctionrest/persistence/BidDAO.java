package com.tcm.tradeauctionrest.persistence;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tcm.tradeauctionrest.domain.Bid;

@Repository
public class BidDAO implements com.tcm.tradeauctionrest.application.dao.BidDAO {

	private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<Bid> bidRowMapper = (resultSet, i) -> {
		Bid bid = new Bid();

		bid.setBidId(resultSet.getString("bidid"));
		bid.setDateP(resultSet.getDate("datep"));
		bid.setPriceEuros(resultSet.getFloat("priceeuros"));
		bid.setNumberBitcoins(resultSet.getFloat("numberbitcoins"));
		bid.setBitcoinsEarned(resultSet.getFloat("bitcoinsearned"));
		bid.setComission(resultSet.getFloat("comission"));
		bid.setUserName(resultSet.getString("profile_username"));

		return bid;
	};
	
	
	public BidDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void saveBid(Bid newBid, String username, String auctionid) {
		final String insertQuery = "insert into bid (bidid, datep, priceeuros, numberbitcoins, bitcoinsearned, profile_username, auction_id, comission) "
				+ "values (?,?,?,?,?,?,?,?)";

		jdbcTemplate.update(insertQuery, newBid.getBidId(), newBid.getDateP(), newBid.getPriceEuros(),
				newBid.getNumberBitcoins(), newBid.getBitcoinsEarned(), username, auctionid, newBid.getComission());
	}

	@Override
	public List<Bid> getBidsByAuctionId(String auctionId) {
		final String query = "select bidid, datep, priceeuros, numberbitcoins, bitcoinsearned, profile_username, comission "
				+ "from bid " + "where auction_id = ? " + "order by priceeuros DESC";
		return this.jdbcTemplate.query(query, bidRowMapper, auctionId);
	}

	@Override
	public void updateBid(Bid bid) {
		final String update = "UPDATE bid SET bitcoinsearned = ?, comission = ? where bidid = ?";

		this.jdbcTemplate.update(update, bid.getBitcoinsEarned(), bid.getComission(), bid.getBidId());
		
	}

	@Override
	public List<Bid> getAllUserBids(String username) {
		final String query = "SELECT bidid, datep, priceeuros, numberbitcoins, bitcoinsearned, profile_username, comission FROM bid where profile_username = ?";
		return this.jdbcTemplate.query(query, bidRowMapper, username);
	}

	@Override
	public List<Bid> getAllBidsWon(String username) {
		final String query = "SELECT bidid, datep, priceeuros, numberbitcoins, bitcoinsearned, comission, profile_username "
				+ "FROM bid " + "WHERE comission > 0 AND profile_username = ?";
		return this.jdbcTemplate.query(query, bidRowMapper, username);
	}

}
