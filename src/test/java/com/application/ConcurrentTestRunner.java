package com.application;

import static com.jayway.restassured.RestAssured.given;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.matchers.JUnitMatchers.*;
import org.junit.Test;

public class ConcurrentTestRunner {
	int counter = 0;
	Date startTime;
	Date endTime;

	@Test
	public final void runConcurrentGetRequest() throws InterruptedException {
		ExecutorService exec = Executors.newFixedThreadPool(10);
		System.out.println("Rest GET Details :");
		for (int i = 0; i < 10; i++) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					startTime = new Date();
					TestRestGetApi_ReturnsStatusOk();
					endTime = new Date();
					counter = counter + 1;
					System.out.println("Execution count " + counter + " " + "Time Difference "
							+ (endTime.getTime() - startTime.getTime()));
				}
			});
		}
		exec.shutdown();
		exec.awaitTermination(50, TimeUnit.SECONDS);
	}

	private void TestRestGetApi_ReturnsStatusOk() {
		given().header("X-Surya-Email-Id", "suhas.hns@gmail.com").when()
				.get("http://surya-interview.appspot.com/message").then().statusCode(200)
				.body(containsString("suhas.hns@gmail.com"));
	}

	@Test
	public final void runConcurrentPostRequest() throws InterruptedException {
		ExecutorService exec = Executors.newFixedThreadPool(10);
		System.out.println("Rest POST Details :");

		for (int i = 0; i < 10; i++) {
			exec.execute(new Runnable() {

				@Override
				public void run() {
					startTime = new Date();
					TestRestPostApi_ReturnsStatusOk();
					endTime = new Date();
					counter = counter + 1;
					System.out.println("Execution count " + counter + " " + "Time Difference "
							+ (endTime.getTime() - startTime.getTime()));
				}
			});
		}
		exec.shutdown();
		exec.awaitTermination(50, TimeUnit.SECONDS);
	}

	private void TestRestPostApi_ReturnsStatusOk() {
		Map<String, String> request = new HashMap<>();
		request.put("emailId", "suhas.hns@gmail.com");
		request.put("uuid", "53d30386-a97c-4ac8-9b3f-bfb2eca4ddd0");

		given().contentType("application/json").body(request).when().post("http://surya-interview.appspot.com/message")
				.then().statusCode(200).body(containsString("Success"));
	}
}