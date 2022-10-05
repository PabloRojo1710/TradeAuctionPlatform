package com.tcm.tradeauctionrest.persistence;

import java.util.ArrayList;
import java.util.List;

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tcm.tradeauctionrest.domain.Auction;

@Repository
public class AuctionDAO implements com.tcm.tradeauctionrest.application.dao.AuctionDAO{
	
	
	private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<Auction> auctionRowMapper = (resultSet, i) -> {
		Auction auction = new Auction();

		auction.setId(resultSet.getString("id"));
		auction.setBitcoins(resultSet.getFloat("bitcoins"));
		auction.setStartingDate(resultSet.getDate("startingdate"));
		auction.setEndingDate(resultSet.getDate("endingdate"));
		auction.setInitialPrice(resultSet.getFloat("initialprice"));
		auction.setClosed(Boolean.parseBoolean(resultSet.getString("closed")));
		auction.setBids(new ArrayList<>());

		return auction;
	};
	
	ResultSetExtractorImpl<Auction> auctionsRowMapper = JdbcTemplateMapperFactory.newInstance().addKeys("id")
			.newResultSetExtractor(Auction.class);
	
	
	public AuctionDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public List<Auction> getAllActiveAuctions() {
		final String query = "select profile_username, id, bitcoins, startingdate, endingdate, initialprice, closed "
				+ "from auction " + "where sysdate > startingdate AND sysdate < endingDate AND closed='false'";
		return this.jdbcTemplate.query(query, auctionRowMapper);
	}

	public List<Auction> getAllToCloseAuctions() {
		final String query = "select profile_username, id, bitcoins, startingdate, endingdate, initialprice, closed from auction where sysdate > endingDate AND closed = 'false'";
		return this.jdbcTemplate.query(query, auctionRowMapper);
	}
	
	@Override
	public void createAuction(Auction auction, String username) {
		final String insert = "INSERT INTO auction (profile_username, id, bitcoins, startingdate, endingdate, initialprice, closed) VALUES (?,?,?,?,?,?,'false')";
		jdbcTemplate.update(insert, username, auction.getId(), auction.getBitcoins(), auction.getStartingDate(),
				auction.getEndingDate(), auction.getInitialPrice());
	}

	@Override
	public List<Auction> listProfileAuctions(String username) {
		final String query = "select id, bitcoins, startingdate, endingdate, initialprice, closed from auction where profile_username = ?";
		return this.jdbcTemplate.query(query, auctionRowMapper, username);
	}

	@Override
	public void updateAuction(Auction auction) {
		final String update = "UPDATE auction SET closed = ? WHERE id = ?";
		this.jdbcTemplate.update(update, "true", auction.getId());
	}

	@Override
	public Auction getAuction(String auctionid) {
		final String query = "SELECT profile_username, id, bitcoins, startingdate, endingdate, initialprice, closed from auction WHERE id = ?";
		return this.jdbcTemplate.query(query, auctionRowMapper, auctionid).get(0);
	}

	@Override
	public List<Auction> getAllAuctionsAndBids(String username) {
		final String query = "SELECT a.id as id, a.bitcoins as bitcoins, a.startingdate as startingDate, a.endingdate as endingDate, a.initialprice as initialPrice, a.closed AS isClosed, "
				+ "b.profile_username as bids_userName, b.bidid as bids_bidId, b.datep as bids_dateP, b.priceeuros as bids_priceEuros, b.numberbitcoins as bids_numberBitcoins, b.bitcoinsearned as bids_bitcoinsEarned, b.comission as bids_comission "
				+ "FROM auction a LEFT JOIN bid b ON a.id = b.auction_id "
				+ "WHERE a.profile_username = ? AND closed = 'true'";
		List<Auction> result = this.jdbcTemplate.query(query, auctionsRowMapper, username);
		result.stream().forEach(this::cleanEmptyBids);
		return result;
	}
	
	private void cleanEmptyBids(Auction auction) {
		boolean hasNullBids = auction.getBids().stream().anyMatch(l -> l.getBidId() == null);
		if (hasNullBids) {
			auction.setBids(new ArrayList<>());
		}
	}

}
