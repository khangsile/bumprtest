package com.llc.bumpr.test.project.unit;

import junit.framework.TestCase;

import com.llc.bumpr.sdk.models.Request;

public class RequestTest extends TestCase {

	public void setUp() {
		
	}
	
	public void testBuildRequest() {
		try {
			Request r = new Request.Builder()
							.setTrip(null)
							.build();
			fail("Should have thrown IllegalStateException");
		} catch (IllegalStateException e) {
			assertTrue("Should throw IllegalStateException", e instanceof IllegalStateException);
		}
	}
}
