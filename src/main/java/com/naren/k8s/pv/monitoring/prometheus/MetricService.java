package com.naren.k8s.pv.monitoring.prometheus;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

public class MetricService {

	public static String metrics() throws IOException {
		Writer writer = new StringWriter();
		TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
		return writer.toString();
	}

	private MetricService() {
	}

}
