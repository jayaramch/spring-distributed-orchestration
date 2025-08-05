package com.test.distributed.poc.controller;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.distributed.poc.kafka.models.PodDetails;
import com.test.distributed.poc.kafka.repository.ClusterRepository;
import com.test.distributed.poc.kafka.request.PodRequest;
import com.test.distributed.poc.kafka.response.SequenceGenerator;
import com.test.distributed.poc.kafka.response.ServicesDetails;
import com.test.distributed.poc.kafka.service.KafkaProducer;
import com.test.distributed.poc.kafka.service.MockDBService;
import com.test.distributed.poc.kafka.utils.MockDBUtils;
import com.test.distributed.poc.service.MicroserviceLauncher;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/orchestrate")
public class MicroserviceRestController {
	
	private Logger logger = LoggerFactory.getLogger(MicroserviceRestController.class);

	private final MicroserviceLauncher launcher;

	public MicroserviceRestController(MicroserviceLauncher launcher) {
		this.launcher = launcher;
	}

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	private MockDBService mockDBService;

	@Autowired
	private ClusterRepository clusterRepository;

	@Autowired
	private MongoOperations mongoOperations;

	@GetMapping("/start")
	public String startChild(HttpServletRequest request) {
		try {

			Integer port = 9090;
			Integer replicas = 2;
			List<PodDetails> pods = new ArrayList<>();
			ServicesDetails servicesDetails = new ServicesDetails();
			for (int i = 0; i < replicas; i++) {
				PodDetails podDetails = launcher.startChildService(port + i);
				podDetails.setIpAddress(request.getRemoteAddr());
				pods.add(podDetails);
			}

			servicesDetails.setId(generateSequence("kub_cluster"));
			servicesDetails.setName("cluster-1");
			servicesDetails.setImage("image-1");
			servicesDetails.setHealth("95");
			servicesDetails.setStatus("ACTIVE");
			servicesDetails.setPods(pods.size());
			servicesDetails.setDescription("Cluster in kuberentes");
			servicesDetails.setCreated_at(Calendar.getInstance().getTime());
			servicesDetails.setUpdated_at(Calendar.getInstance().getTime());
			servicesDetails.setPodDetails(pods);
			MockDBUtils dbUtils = new MockDBUtils();
			dbUtils.setPodStarted(true);
			dbUtils.setReplicas(replicas);
			mockDBService.updateDBMock(dbUtils);

			clusterRepository.save(servicesDetails);

			return "Child service started on port " + port;
		} catch (IOException e) {
			return "Failed to start child service: " + e.getMessage();
		}
	}

	@GetMapping("/details")
	public ResponseEntity<List<ServicesDetails>> podDetails() {
		return ResponseEntity.ok(clusterRepository.findAll());
	}
	
	@PostMapping("/pod/increment")
	public ResponseEntity<ServicesDetails> increment(@RequestBody PodRequest podRequest, HttpServletRequest request) throws IOException {
		
		Optional<ServicesDetails> optional = clusterRepository.findById(podRequest.getClusterId());
		ServicesDetails servicesDetails = optional.get();
		PodDetails podDetails = launcher.startChildService(podRequest.getPodPort());
		podDetails.setIpAddress(request.getRemoteAddr());
		servicesDetails.getPodDetails().add(podDetails);
		clusterRepository.save(servicesDetails);
		return ResponseEntity.ok(servicesDetails);
	}
	
	@PostMapping("/pod/decrement")
	public ResponseEntity<ServicesDetails> decrement(@RequestBody PodRequest podRequest, HttpServletRequest request) throws IOException {
		
		Optional<ServicesDetails> optional = clusterRepository.findById(podRequest.getClusterId());
		ServicesDetails servicesDetails = optional.get();
		CopyOnWriteArrayList<PodDetails> podDetails = new CopyOnWriteArrayList<>(servicesDetails.getPodDetails());
		for (PodDetails pod : podDetails) {
	        if (pod.getPort().equals(podRequest.getPodPort())) {
	        	if(launcher.killTheProcess(podRequest.getPodPort())) {
	        		logger.info("PROCESS KILLED SUCCESSFULLY!");
	        		podDetails.remove(pod);
	        	} else {
	        		logger.info("FAILED TO KILL THE PROCESS!");
	        	}
	        }
	    }
		servicesDetails.setPodDetails(podDetails);
		
		clusterRepository.save(servicesDetails);
		return ResponseEntity.ok(servicesDetails);
	}

	@GetMapping("/kafka/send")
	public String sendMessage() {
		// kafkaProducer.sendMessage("Sending message Kafka Topic");
		return "Message sent to Kafka topic";
	}

	public String generateSequence(String seqName) {
		SequenceGenerator sequenceGenerator = mongoOperations.findAndModify(query(where("_id").is("kub_cluster")),
				new Update().inc("seq", 1), options().returnNew(true).upsert(true), SequenceGenerator.class);
		return !Objects.isNull(sequenceGenerator) ? sequenceGenerator.getSeq() + "" : "1000000";
	}
}
