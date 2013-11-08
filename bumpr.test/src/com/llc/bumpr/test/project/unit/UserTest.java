package com.llc.bumpr.test.project.unit;

import junit.framework.TestCase;

import com.llc.bumpr.sdk.models.User;

public class UserTest extends TestCase {
	User user;
	
	public void setUp() {
		user = new User.Builder<User>(new User())
				.setFirstName("Khang")
				.setLastName("Le")
				.setEmail("khangsile@gmail.com")
				.setPhoneNumber("859-992-7764")
				.setCity("Edgewood")
				.setState("KY")
				.setDescription("Pains fans are crazy.")
				.build();
	}
	
	public void testAddRequest() {
		try {
			user.addRequest(null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue("Should throw IllegalArgumentException", e instanceof IllegalArgumentException);
		}
	}
	
	public void testUpdateUser() {
		try {
			user.update(null);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue("Should throw IllegalArgumentException", e instanceof IllegalArgumentException);
		}
	}
}
