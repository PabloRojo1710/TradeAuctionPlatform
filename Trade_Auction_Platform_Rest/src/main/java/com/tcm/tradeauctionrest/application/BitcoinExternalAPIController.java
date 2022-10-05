package com.tcm.tradeauctionrest.application;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BitcoinExternalAPIController {

	private RestTemplate restTemplate;
	private final String apiUrl = "http://stockmarkettrading.azurewebsites.net/stocks/bitcoins/";
	private final String teamName = "SKERE";

	public BitcoinExternalAPIController(RestTemplate _restTemplate) {
		this.restTemplate = _restTemplate;
	}

	public float queryBitcoinPrice() {

		var jsonObject = new JSONObject(this.restTemplate.getForObject(this.apiUrl + this.teamName, String.class));
		return jsonObject.getFloat("unitPriceInEur");

	}

	public float buyBitcoin(float amount) {

		var requestJsonObject = new JSONObject().put("groupId", teamName).put("amount", amount);
		var jsonObject = new JSONObject(
				this.restTemplate.postForObject(this.apiUrl, requestJsonObject.toString(), String.class));

		return jsonObject.getFloat("unitPriceInEur");
	}

}
