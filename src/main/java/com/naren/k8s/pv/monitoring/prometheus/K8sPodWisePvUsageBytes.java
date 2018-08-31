package com.naren.k8s.pv.monitoring.prometheus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.SimpleCollector;

public class K8sPodWisePvUsageBytes {

	private static final io.prometheus.client.Gauge.Builder builder = Gauge.build().namespace("k8s").subsystem("pod")
			.name("pv_usage_bytes").labelNames("pod", "namespace", "uid", "host", "cluster","name")
			.help("PV usage for the pod in bytes");
	private static Gauge gauge = builder.register(CollectorRegistry.defaultRegistry);

	public static void register(CollectorRegistry registry) {
		gauge = builder.register(registry);
	}

	public static void recordMetric(double value, String[] labelValues) {
		addLabels(gauge, labelValues).set(value);
	}

	private static <T> T addLabels(SimpleCollector<T> collector, String... labels) {
		List<String> allLabels = new ArrayList<>();
		Collections.addAll(allLabels, labels);
		return collector.labels(allLabels.toArray(new String[0]));
	}

}
