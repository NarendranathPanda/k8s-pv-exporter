package com.naren.k8s.pv.monitoring.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naren.k8s.pv.monitoring.api.access.StringResponseAPIGateWay;
import com.naren.k8s.pv.monitoring.api.access.StringResponseFromStaticFileReader;
import com.naren.k8s.pv.monitoring.api.config.K8sPvExporterConfig;
import com.naren.k8s.pv.monitoring.exception.RestEndPointNotAccessableException;
import com.naren.k8s.pv.monitoring.prometheus.K8sPodWisePvAvailableBytes;
import com.naren.k8s.pv.monitoring.prometheus.K8sPodWisePvCapacityBytes;
import com.naren.k8s.pv.monitoring.prometheus.K8sPodWisePvUsageBytes;

@Component
public class K8sPvProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(K8sPvProcessor.class.getName());

	public static String LAST_SCRAP = "NA";

	public static String FILE_PATH = "classpath:download/summary.json";

	@Autowired
	StringResponseFromStaticFileReader fileReader;

	@Autowired
	StringResponseAPIGateWay api;

	@Autowired
	K8sPvExporterConfig config;

	public void process(String host, String clusterName) throws RestEndPointNotAccessableException, IOException {
		LOGGER.debug("k8s pv monitor Report Process Start");
		LAST_SCRAP = System.currentTimeMillis() + "";
		String fileContent = getStatSummery(host);
		if (null != fileContent) {
			recordMetric(fileContent, host, clusterName);
		}
		LOGGER.debug("k8s pv monitor Report Process Ends");
	}

	private String getStatSummery(String host) {
		if (config.isTestEnvironment())
			return fileReader.getFileContent(FILE_PATH);
		else
			return getFromUrls(host);
	}

	private String getFromUrls(String host) {
		LOGGER.info("Connecting to " + host);
		String exchange = null;
		try {
			exchange = api.exchange(K8sPvExporterConfig.getStatSummaryUrl(host), config.getClientHttpRequestFactory(),
					config.getHttpHeaders());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return exchange;
	}

	public void recordMetric(String metrics, String host, String clusterName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(metrics);
		JsonNode pods = root.path("pods");
		for (JsonNode pod : pods) {
			String[] podRefInfo = getPodInfo(pod, host, clusterName);
			recordMetricForCapacityBytes(pod, podRefInfo);
			recordMetricForAvailableBytes(pod, podRefInfo);
			recordMetricForUsageBytes(pod, podRefInfo);
		}
	}

	private String[] getPodInfo(JsonNode pod, String host, String clusterName) {
		return getPodRefInfo(pod, host, clusterName);
	}

	private void recordMetricForAvailableBytes(JsonNode pod, String[] podRefInfo) {
		JsonNode volumeInfo = pod.path("volume");
		for (JsonNode volume : volumeInfo) {
			double availableBytes = getVolumeInfo(volume, "availableBytes");
			String pvName = getValue(volume, "name");
			K8sPodWisePvAvailableBytes.recordMetric(availableBytes, getLables(podRefInfo, pvName));
		}
	}

	private String[] getLables(String[] podRefInfo, String pvName) {
		List<String> allLabels = new ArrayList<>();
		Collections.addAll(allLabels, podRefInfo);
		allLabels.add(pvName);
		return allLabels.toArray(new String[0]);
	}

	private void recordMetricForCapacityBytes(JsonNode pod, String[] podRefInfo) {
		JsonNode volumeInfo = pod.path("volume");
		for (JsonNode volume : volumeInfo) {
			double capacityBytes = getVolumeInfo(volume, "capacityBytes");
			String pvName = getValue(volume, "name");
			K8sPodWisePvCapacityBytes.recordMetric(capacityBytes, getLables(podRefInfo, pvName));
		}
	}

	private void recordMetricForUsageBytes(JsonNode pod, String[] podRefInfo) {

		JsonNode volumeInfo = pod.path("volume");
		for (JsonNode volume : volumeInfo) {
			double usedBytes = getVolumeInfo(volume, "usedBytes");
			String pvName = getValue(volume, "name");
			K8sPodWisePvUsageBytes.recordMetric(usedBytes, getLables(podRefInfo, pvName));
		}
	}

	private String[] getPodRefInfo(JsonNode pod, String host, String clusterName) {
		JsonNode podRef = pod.path("podRef");
		String namespace = getValue(podRef, "namespace");
		String name = getValue(podRef, "name");
		String uid = getValue(podRef, "uid");
		String[] labels = { name, namespace, uid, host, clusterName };
		return labels;
	}

	private String getValue(JsonNode node, String name) {
		return node.path(name).asText();
	}

	private double getVolumeInfo(JsonNode volumeInfo, String type) {
		return volumeInfo.path(type).asDouble();
	}

}
