package com.naren.k8s.pv.monitoring.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.naren.k8s.pv.monitoring.exception.RestEndPointNotAccessableException;
import com.naren.k8s.pv.monitoring.util.Util;

@Component
public class ReportScheduledTasks {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportScheduledTasks.class.getName());

	@Autowired
	K8sPvProcessor processor;

	@Value("${cluster.name.list}")
	String clusterNameList;

	@Value("${cluster.ip.list}")
	String clusterIpList;

	@Value("${host.ip.list}")
	String hostIpList;

	@Scheduled(fixedDelay = 60000)
	public void readPvUsageHost() throws RestEndPointNotAccessableException, IOException {
		long t = System.currentTimeMillis();
		LOGGER.info("############# Task : stats summary metrics " + Util.getDate() + " starts.#############");
		String[] clusterNames = clusterNameList.split(",");
		String[] clusterIps = clusterIpList.split(",");
		String[] clusterTohostIps = hostIpList.split(":");
		for (int i = 0; i < clusterNames.length; i++) {
			String[] host = clusterTohostIps[i].split(",");
			for (int j = 0; j < host.length; j++) {
				processor.process(clusterIps[i] + "." + host[j], clusterNames[i]);
			}

		}
		LOGGER.info("############# Task : stats summary metrics " + Util.getDate() + " finished in "
				+ (System.currentTimeMillis() - t) / 1000 + " sec.#############");
	}

}