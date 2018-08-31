package com.naren.k8s.pv.monitoring.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.naren.k8s.pv.monitoring.prometheus.ExporterInfo;
import com.naren.k8s.pv.monitoring.prometheus.MetricService;

@RestController
@RequestMapping("/pv/")
public class MonitorServiceController {

	public static final String PROMETHEUS_CONTENTTYPE = "text/plain; version=0.0.4; charset=utf-8";

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServiceController.class.getName());

	@Value("${stat.summary.url}")
	String url;

	public MonitorServiceController() {
		ExporterInfo.inc();
	}

	@RequestMapping(method = RequestMethod.GET, produces = { PROMETHEUS_CONTENTTYPE }, path = "metrics")
	public String metrics() {
		String metrics = "Metrics";
		try {
			LOGGER.debug("Metrics Requested.");
			metrics = MetricService.metrics();
		} catch (IOException e) {
			metrics = "Prometheus writer failed to write out the text version 0.0.4 of the given MetricFamilySamples.";
			LOGGER.error(metrics, e);
		}
		LOGGER.debug("Metrics provided.");
		return metrics;
	}

}
