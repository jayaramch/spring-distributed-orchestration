package com.test.distributed.poc.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

	public PodDetails startChildService(Integer port) throws IOException {
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
		podDetails.setPodName("Springboot-practice");
		podDetails.setPodStatus("RUNNING");

		kafkaProducer.sendMessage(podDetails);
		return podDetails;
	}

	public boolean killTheProcess(Integer port) {
		String findCommand = "cmd.exe /c netstat -aon | findstr :" + port;

		try {
			Process findProcess = Runtime.getRuntime().exec(findCommand);
			BufferedReader reader = new BufferedReader(new InputStreamReader(findProcess.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("LISTENING")) {
					String[] tokens = line.trim().split("\\s+");
					String pid = tokens[tokens.length - 1];
					System.out.println("Killing process with PID: " + pid);
					Runtime.getRuntime().exec("cmd.exe /c taskkill /PID " + pid + " /F");
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
