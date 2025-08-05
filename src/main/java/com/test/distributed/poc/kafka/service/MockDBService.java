package com.test.distributed.poc.kafka.service;

import org.springframework.stereotype.Service;

import com.test.distributed.poc.kafka.utils.MockDBUtils;

@Service
public class MockDBService {

	private MockDBUtils mockDBUtils = null;
	
	public MockDBUtils getDBMock() {
		return mockDBUtils;
	}
	
	public void updateDBMock(MockDBUtils mockDBUtils) {
		this.mockDBUtils = mockDBUtils;
	}
}
