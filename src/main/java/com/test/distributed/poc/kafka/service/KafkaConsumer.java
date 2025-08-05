package com.test.distributed.poc.kafka.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.test.distributed.poc.kafka.models.PodDetails;
import com.test.distributed.poc.kafka.utils.CommonUtils;
import com.test.distributed.poc.service.MicroserviceLauncher;

//@ConditionalOnProperty(havingValue = "true", name="${enable.kafka.access:true}")
@Service
public class KafkaConsumer {

	private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	private final Map<Integer, String> cache = new HashMap<>();

	@Autowired
	private MicroserviceLauncher launcher;

	@KafkaListener(topics = "test-topic", groupId = "my-group")
	public void listen(PodDetails message) throws IOException {
		logger.info("Received Pod Details : PORT {} MESSAGE {} ", message.getPort(), message.getMessage());
		logger.info("Starting new microservice");
		Integer port = message.getPort();
		String event = message.getEvent();
		
		switch (event) {
			case CommonUtils.SUCCESS_EVENT:
				cache.remove(port);
				break;
			case CommonUtils.FAILED_EVENT:
				initiatePod(port);
				break;
			default:
				break;
		}
	}

	private void initiatePod(Integer port) throws IOException {
		if (cache.containsKey(port)) {
			logger.info("MicroService initialization in progress with PORT {} ", port);
		} else {
			logger.info("MicroService Initialization Started. PORT {} ", port);
			cache.put(port, "PORT:" + port);
			launcher.startChildService(port);
		}
	}
}
