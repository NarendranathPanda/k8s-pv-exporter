package com.naren.k8s.pv.monitoring;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Enumeration;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MonitorServiceControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private Optional<Collector.MetricFamilySamples> findRecordedMetric(String name,
			CollectorRegistry collectorRegistry) {
		Enumeration<Collector.MetricFamilySamples> samples = collectorRegistry.metricFamilySamples();
		while (samples.hasMoreElements()) {
			Collector.MetricFamilySamples sample = samples.nextElement();
			if (sample.name.equals(name)) {
				return Optional.of(sample);
			}
		}
		return Optional.empty();
	}

	private Collector.MetricFamilySamples findRecordedMetricOrThrow(String name, CollectorRegistry collectorRegistry) {
		Optional<Collector.MetricFamilySamples> result = findRecordedMetric(name, collectorRegistry);
		if (!result.isPresent()) {
			throw new IllegalArgumentException("Could not find metric with name: " + name);
		}
		return result.get();
	}

	@Test
	public void metric() throws Exception {
		mockMvc.perform(get("/pv/metrics").header("Host", "local")).andExpect(status().isOk()).andExpect(content()
				.contentType(com.naren.k8s.pv.monitoring.controller.MonitorServiceController.PROMETHEUS_CONTENTTYPE));
		assertEquals(1,
				findRecordedMetricOrThrow("k8s_pv_exporter_info", CollectorRegistry.defaultRegistry).samples.size());

	}

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

}
