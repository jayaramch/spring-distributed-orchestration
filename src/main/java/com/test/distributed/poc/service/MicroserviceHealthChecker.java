package com.test.distributed.poc.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.test.distributed.poc.kafka.models.PodDetails;
import com.test.distributed.poc.kafka.service.KafkaProducer;
import com.test.distributed.poc.kafka.service.MockDBService;
import com.test.distributed.poc.kafka.utils.CommonUtils;
import com.test.distributed.poc.kafka.utils.MockDBUtils;

@Service
public class MicroserviceHealthChecker {

	private Logger logger = LoggerFactory.getLogger(MicroserviceHealthChecker.class);

//	private RestTemplate restTemplate = new RestTemplate();

	private final WebClient webClient = WebClient.builder().build();

	@Autowired
	private MockDBService mockDBService;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Scheduled(fixedDelay = 10000)
	public void checkHealth() {
		MockDBUtils mockDBUtils = mockDBService.getDBMock();
		if (mockDBUtils != null && mockDBService.getDBMock().isPodStarted()) {
			for (int i = 0; i < mockDBUtils.getReplicas(); i++) {
				Integer port = CommonUtils.PORT_NUMBER + i;
				String serverUrl = CommonUtils.DEFAULT_HOT + port + CommonUtils.ACTUATOR_HEALTH;
				logger.info("SERVER URL {} ", serverUrl);
				webClient.get().uri(serverUrl).retrieve().bodyToMono(Map.class).doOnNext(body -> {
					logger.info("SERVER UP & RUNNING {} ", port);
				}).doOnError(error -> {
					logger.info("SERVER DOWN {} ", port);
					PodDetails podDetails = new PodDetails();
					podDetails.setPort(port);
					podDetails.setMessage("Health check failed");
					podDetails.setEvent(CommonUtils.FAILED_EVENT);

					kafkaProducer.sendMessage(podDetails);
				}).subscribe();
			}
		}
	}
}
