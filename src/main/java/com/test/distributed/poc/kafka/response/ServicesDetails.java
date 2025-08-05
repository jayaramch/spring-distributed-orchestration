package com.test.distributed.poc.kafka.response;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.test.distributed.poc.kafka.models.PodDetails;

@Document(collection = "service_details")
public class ServicesDetails {
	@Id
	private String id;
	private String name;
	private String image;
	private String status;
	private Date created_at;
	private Date updated_at;
	private String description;
	private String ready;
	private String health;
	private String nodes;
	private Integer pods;
	private List<PodDetails> podDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReady() {
		return ready;
	}

	public void setReady(String ready) {
		this.ready = ready;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public Integer getPods() {
		return pods;
	}

	public void setPods(Integer pods) {
		this.pods = pods;
	}

	public List<PodDetails> getPodDetails() {
		return podDetails;
	}

	public void setPodDetails(List<PodDetails> podDetails) {
		this.podDetails = podDetails;
	}

}
