package com.naren.k8s.pv.monitoring.api.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

@Component
public class K8sPvExporterConfig {

	@Value("${stat.summary.url}")
	String url;
	@Value("${connection.timeout}")
	int timeout;
	@Value("${env}")
	String env;

	@Value("${exporter.version}")
	String version;

	HttpHeaders httpHeaders;

	ClientHttpRequestFactory clientHttpRequestFactory;

	public static String ENV_TEST = "Test";
	public static String ENV_PRODUCTION = "Production";

	private static String STATE_SUMMARY_PATH = ":10255/stats/summary";

	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public ClientHttpRequestFactory getClientHttpRequestFactory() {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).setAuthenticationEnabled(true).build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		return new HttpComponentsClientHttpRequestFactory(client);
	}

	public void setClientHttpRequestFactory(ClientHttpRequestFactory clientHttpRequestFactory) {
		this.clientHttpRequestFactory = clientHttpRequestFactory;
	}

	public String getUrl() {
		return url;
	}

	public String[] getUrls() {
		return url.split(",");
	}

	public int getTimeout() {
		return timeout;
	}

	public String getEnvironment() {
		return env;
	}

	public boolean isTestEnvironment() {
		return ENV_TEST.equals(env);
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static String getStatSummaryUrl(String host) {
		return "http://" + host + STATE_SUMMARY_PATH;
	}

}
