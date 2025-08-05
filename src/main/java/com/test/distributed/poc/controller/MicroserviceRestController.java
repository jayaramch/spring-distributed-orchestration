package com.test.distributed.poc.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.distributed.poc.kafka.service.KafkaProducer;
import com.test.distributed.poc.kafka.service.MockDBService;
import com.test.distributed.poc.kafka.utils.MockDBUtils;
import com.test.distributed.poc.service.MicroserviceLauncher;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/orchestrate")
public class MicroserviceRestController {

	private final MicroserviceLauncher launcher;

	public MicroserviceRestController(MicroserviceLauncher launcher) {
		this.launcher = launcher;
	}

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	private MockDBService mockDBService;

	@GetMapping("/start")
	public String startChild() {
		try {
			Integer port = 9090;
			Integer replicas = 2;
			for (int i = 0; i < replicas; i++) {
				launcher.startChildService(port + i);
			}

			MockDBUtils dbUtils = new MockDBUtils();
			dbUtils.setPodStarted(true);
			dbUtils.setReplicas(replicas);
			mockDBService.updateDBMock(dbUtils);

			return "Child service started on port " + port;
		} catch (IOException e) {
			return "Failed to start child service: " + e.getMessage();
		}
	}

	@GetMapping("/kafka/send")
	public String sendMessage() {
		// kafkaProducer.sendMessage("Sending message Kafka Topic");
		return "Message sent to Kafka topic";
	}
}
