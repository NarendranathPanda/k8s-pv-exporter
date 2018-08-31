package com.naren.k8s.pv.monitoring.api.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.naren.k8s.pv.monitoring.exception.RestEndPointNotAccessableException;
import com.naren.k8s.pv.monitoring.util.Util;

@Component
public class StringResponseAPIGateWay {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringResponseAPIGateWay.class.getName());

	public String exchange(String url, ClientHttpRequestFactory clientHttpRequestFactory, HttpHeaders httpHeader) {
		long t = System.currentTimeMillis();
		LOGGER.debug("Accessing the URL: " + url);
		Util.validateString("url", url);
		String report = "";
		try {
			RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
			ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET,
					new HttpEntity<String>(httpHeader), String.class);
			report = exchange.getBody();
		} catch (org.springframework.web.client.ResourceAccessException e) {
			throw new RestEndPointNotAccessableException(url, e);
		}
		LOGGER.info("Extract kube stats summary Successful . ");
		LOGGER.info("Report received in " + (System.currentTimeMillis() - t) / 1000 + " seconds.");
		return report;
	}

}
