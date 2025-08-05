package com.test.distributed.poc.kafka.models;

import java.io.Serializable;

public class PodDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer port;
	private String jarpath;
	private String event;
	private String message;

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getJarpath() {
		return jarpath;
	}

	public void setJarpath(String jarpath) {
		this.jarpath = jarpath;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
