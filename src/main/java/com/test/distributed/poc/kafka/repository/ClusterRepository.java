package com.test.distributed.poc.kafka.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.test.distributed.poc.kafka.response.ServicesDetails;

@Repository
public interface ClusterRepository extends MongoRepository<ServicesDetails, String>{

}
