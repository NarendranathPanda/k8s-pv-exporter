package com.naren.k8s.pv.monitoring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.naren.k8s.pv.monitoring.api.config.K8sPvExporterConfig;
import com.naren.k8s.pv.monitoring.model.EnvironmentParameter;

@Controller
public class NdacExporterController {

	@Autowired
	K8sPvExporterConfig ndacPmoConfig;

	

	@GetMapping("/")
	public String about(Model model) {
		model.addAttribute("envs", getDetails());
		return "index";
	}

	private List<EnvironmentParameter> getDetails() {
		List<EnvironmentParameter> envParams = new ArrayList<>();
		envParams.add(getEnvironmentParameter("Excution Environment", ndacPmoConfig.getEnv()));
		envParams.add(getEnvironmentParameter("NDAC Exporter Version", ndacPmoConfig.getVersion()));
		envParams.add(getEnvironmentParameter("Metric", "Metric", "/pv/metrics"));

		return envParams;
	}

	private EnvironmentParameter getEnvironmentParameter(String name, String value, String url) {
		EnvironmentParameter env = new EnvironmentParameter();
		env.setName(name);
		env.setUrl(url);
		env.setValue(value);
		return env;
	}

	private EnvironmentParameter getEnvironmentParameter(String name, String value) {
		return getEnvironmentParameter(name, value, null);
	}

}
