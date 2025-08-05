package com.test.distributed.poc.kafka.utils;

public class MockDBUtils {

	private boolean isPodStarted;
	private Integer replicas;

	public Integer getReplicas() {
		return replicas;
	}

	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}

	public boolean isPodStarted() {
		return isPodStarted;
	}

	public void setPodStarted(boolean isPodStarted) {
		this.isPodStarted = isPodStarted;
	}

}
