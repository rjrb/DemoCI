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
		final Request r1 = new Request("2018-03-23", 10, "col");

		// Prueba 1
		Assert.assertEquals(
			"La fecha esperada no coincide",
			LocalDate.of(2018, Month.APRIL, 10).toString(),
			lh.handleRequest(r1, null)
		);

		// Petición 2
		final Request r2 = new Request("201803", 10, "col");

		// Prueba 2
		Assert.assertNull("Se esperaba nulo", lh.handleRequest(r2, null));
	}

}
