package com.test.distributed.poc.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.distributed.poc.kafka.models.PodDetails;
import com.test.distributed.poc.kafka.service.KafkaProducer;
import com.test.distributed.poc.kafka.utils.CommonUtils;

@Service
public class MicroserviceLauncher {

	private Logger logger = LoggerFactory.getLogger(MicroserviceLauncher.class);

	@Autowired
	private KafkaProducer kafkaProducer;

	public void startChildService(Integer port) throws IOException {
		String jarPath = "C:\\workspace_practice\\spring-boot-practice\\target\\spring-boot-practice-0.0.1-SNAPSHOT.jar";

		ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath, "--server.port=" + port);

		builder.inheritIO();
		builder.start();
		logger.info("Started child service on port {} ", port);
		
		PodDetails podDetails = new PodDetails();
		podDetails.setEvent(CommonUtils.SUCCESS_EVENT);
		podDetails.setPort(port);
		podDetails.setJarpath(jarPath);
		podDetails.setMessage("Microservice started Successfully");

		kafkaProducer.sendMessage(podDetails);
	}
}
