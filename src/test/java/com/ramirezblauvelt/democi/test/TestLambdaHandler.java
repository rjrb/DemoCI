package com.ramirezblauvelt.democi.test;

import com.ramirezblauvelt.democi.aws.LambdaHandler;
import com.ramirezblauvelt.democi.aws.Request;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

public class TestLambdaHandler {

	/* Handler */
	private static LambdaHandler lh = new LambdaHandler();

	@Test
	public void testHandler() {
		// Petición 1
		final Request r1 = new Request(LocalDate.of(2018, Month.MARCH, 23), 10, "col");

		// Prueba 1
		Assert.assertEquals(
			"La fecha esperada no coincide",
			LocalDate.of(2018, Month.APRIL, 10),
			lh.handleRequest(r1, null)
		);
	}

}
