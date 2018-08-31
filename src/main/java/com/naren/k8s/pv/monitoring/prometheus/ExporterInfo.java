package com.naren.k8s.pv.monitoring.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

public class ExporterInfo {

	private static final io.prometheus.client.Counter.Builder builder = Counter.build().namespace("k8s")
			.subsystem("pv_exporter").name("info").help("K8s PV Exporter information.");
	private static Counter counter = builder.register(CollectorRegistry.defaultRegistry);

	public static void register(CollectorRegistry registry) {
		counter = builder.register(registry);
	}

	public static void inc() {
		counter.inc();
	}

}
