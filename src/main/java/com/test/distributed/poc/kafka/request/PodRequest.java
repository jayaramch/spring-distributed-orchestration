package com.test.distributed.poc.kafka.request;

public class PodRequest {
	private String clusterId;
	private Integer podPort;

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public Integer getPodPort() {
		return podPort;
	}

	public void setPodPort(Integer podPort) {
		this.podPort = podPort;
	}

}
