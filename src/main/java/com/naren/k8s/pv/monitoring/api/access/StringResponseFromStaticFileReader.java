package com.naren.k8s.pv.monitoring.api.access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.naren.k8s.pv.monitoring.exception.RestEndPointNotAccessableException;
import com.naren.k8s.pv.monitoring.util.Util;

@Component
public class StringResponseFromStaticFileReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringResponseFromStaticFileReader.class.getName());

	public String getFileContent(String filePath) {
		long t = System.currentTimeMillis();
		LOGGER.debug("Accessing the file path: " + filePath);
		Util.validateString("File Path", filePath);
		String report = "";
		File file = null;
		try {
			file = ResourceUtils.getFile(filePath);
			file.exists();
			report = new String(Files.readAllBytes(file.toPath()));
		} catch (FileNotFoundException e) {
			throw new RestEndPointNotAccessableException(filePath, e);
		} catch (IOException e) {
			throw new RestEndPointNotAccessableException(filePath, e);
		}

		LOGGER.debug("Extract Automation Testcase Successful . ");
		LOGGER.info("Report received in " + (System.currentTimeMillis() - t) / 1000 + " seconds.");
		return report;
	}

}
