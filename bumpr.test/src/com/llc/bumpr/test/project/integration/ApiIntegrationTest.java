package com.llc.bumpr.test.project.integration;

import java.util.HashMap;
import java.util.logging.Logger;

import junit.framework.TestCase;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.llc.bumpr.sdk.interfaces.BumprAPI;
import com.llc.bumpr.sdk.lib.BumprClient;
import com.llc.bumpr.sdk.lib.Coordinate;
import com.llc.bumpr.sdk.models.Driver;
import com.llc.bumpr.sdk.models.Login;
import com.llc.bumpr.sdk.models.LoginResponse;
import com.llc.bumpr.sdk.models.Registration;
import com.llc.bumpr.sdk.models.Request;
import com.llc.bumpr.sdk.models.Session;
import com.llc.bumpr.sdk.models.Trip;
import com.llc.bumpr.sdk.models.User;
import com.llc.restrofit.ResponseConverter;

public class ApiIntegrationTest extends TestCase {
	protected User user;
	protected Session session = new Session();
	
	@Override
	protected void setUp() {
		BumprAPI api = BumprClient.api();
		
		String email = "khangsie@asdfeqkjdafsdf.com"; //change this every time or something.
		String password = "handicap";
		
		Registration r = new Registration.Builder()
			.setPassword(password)
			.setPasswordConfirmation(password)
			.setFirstName("Khang")
			.setLastName("Le")
			.setEmail(email) //must change every time!
			.build();
		
		try {
			LoginResponse resp = api.register(r);
			user = resp.getUser();
			session.setAuthToken(resp.getAuthToken());
		} catch (RetrofitError e) {
			LoginResponse resp = api.login(new Login(email, password));
			user = resp.getUser();
			session.setAuthToken(resp.getAuthToken());
		}
	}
	
	public void testValidLogin() {
		BumprAPI api = BumprClient.api();
		try {
			LoginResponse response = api.login(new Login("khangsile@gmail.com", "handicap"));
			
			assertTrue("Login should return session", response.getAuthToken() != null && response.getUser() != null && response.getUser().getDriverProfile() != null);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Login should return false");
		}
	}
	
	public void testInvalidLogin() {
		BumprAPI api = BumprClient.api();
		try {
			LoginResponse response = api.login(new Login("khangsile@gmail.com", "handicap2"));
			fail("Should throw RetrofitError exception");
		} catch (RetrofitError e) {
			assertTrue("Threw RetrofitError exception", e instanceof RetrofitError);
		}
	}
	
	/*public void testValidRegistration() {
		BumprAPI api = BumprClient.api();
		Registration r = new Registration.Builder()
			.setPassword("handicap")
			.setPasswordConfirmation("handicap")
			.setFirstName("Khang")
			.setLastName("Le")
			.setEmail("khangsile@emawilses.com") //must change every time!
			.build();
		try {
			LoginResponse resp = api.register(r);
			assertTrue("Should have returned user and authToken", !resp.getAuthToken().trim().equals("") && resp.getUser() != null);
		} catch (RetrofitError e) {
			e.printStackTrace();
			fail("Should have created user");
		}
	}*/
	
	public void testInvalidRegistration() {
		BumprAPI api = BumprClient.api();
		Registration r = new Registration.Builder()
			.setPassword("handicap")
			.setPasswordConfirmation("handicap")
			.setLastName("Le")
			.setEmail("khangsile@emaile.com")
			.build();
		try {
			LoginResponse resp = api.register(r);
			fail("Should have returned user and authToken");
		} catch (RetrofitError e) {
			
			assertTrue("Should have failed to create user", e instanceof RetrofitError);
		}
	}
	
	public void testUpdateUser() throws Exception {
		BumprAPI api = BumprClient.api();
		
		HashMap<String, Object> user1 = new HashMap<String, Object>();
		
		//user1.put("first_name", user.getFirstName() + "t");
		user1.put("first_name", user.getLastName());
		user1.put("last_name", user.getFirstName());
		
		try {
			User user2 = api.updateUser(session.getAuthToken(), user.getId(), user1);
			assertTrue("Should update user's email", !user.getFirstName().equals(user2.getFirstName()));
		} catch (RetrofitError e) {
			fail("Should have updated the user's email");
		} 
	}
	
	public void testRegisterDriver() {
		BumprAPI api = BumprClient.api();
		Driver driver = api.registerDriver(session.getAuthToken());
		assertTrue("Should create a driver object", driver != null);
	}
	
	public void testGetUser() {
		BumprAPI api = BumprClient.api();
		User user = api.getUser(1);
		
		assertTrue("Should return a user object", user != null);
	}
	
	public void testRequest() throws Exception {
		BumprAPI api = BumprClient.api();
		
		Trip t = new Trip.Builder()
			.setEnd(new Coordinate(14.5f, 15.5f))
			.setStart(new Coordinate(14.5f, 15.5f))
			.build();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("start", t.getStart());
		map.put("end", t.getEnd());
		
		Request r = new Request.Builder()
			.setDriverId(1)
			.setUserId(user.getId())
			.setTrip(t)
			.build();
		
		try {
			Request request = api.request(session.getAuthToken(), r.getDriverId(), map);
			assertTrue("Should return a request object", request != null);
		} catch (RetrofitError e) {
			Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			log.warning(e.getMessage());
			fail("Should not return an error");
		}
	}
	
	public void testSearchDrivers() throws Exception {
		BumprAPI api = BumprClient.api();
		Response resp = api.searchDrivers(85.5, 84.4, 36.6, 36.7);
		
		try {
			String string = ResponseConverter.responseToString(resp);
			assertTrue("Should return a response body", true);
		} catch (Exception e) {
			Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
			//logger.warning(e.)
			e.printStackTrace();
			fail("Should have a response body");
		}
	}
	
	// Unfinished. Ignore case.
	public void testUpdateDriver() throws Exception {
		BumprAPI api = BumprClient.api();
		Driver driver = new Driver();
		try {
			driver = api.registerDriver(session.getAuthToken());
		} catch (Exception e) {
		} finally {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("license_id", "123456");
			api.updateDriver(session.getAuthToken(), driver.getId(), map);
		}
	}
}