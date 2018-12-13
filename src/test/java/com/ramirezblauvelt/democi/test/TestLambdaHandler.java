package com.ramirezblauvelt.democi.test;

import com.ramirezblauvelt.democi.aws.LambdaHandler;
import com.ramirezblauvelt.democi.beans.LambdaRequest;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

public class TestLambdaHandler {

	/* Handler */
	private static LambdaHandler lh = new LambdaHandler();

	@Test
	public void testHandler() {
		// Datos de entrada
		final String f = "2018-03-23";
		final int d = 10;
		final String p = "col";
		final String c = "Colombia";

		// Resultado
		final LocalDate n = LocalDate.of(2018, Month.APRIL, 10);
		final Message m = new FormattedMessage("El resultado de sumar '{}' días hábiles en '{}' a la fecha '{}' es: '{}'", d, c, f, n);

		// Petición
		final LambdaRequest r = new LambdaRequest(f, d, p);

		// Prueba
		Assert.assertThat(
			lh.handleRequest(r, null).getResultado(),
			CoreMatchers.containsString(n.toString())
		);
	}

	@Test
	public void testHandlerErrorFecha() {
		// Datos de entrada
		final String f = "20180323";
		final int d = 10;
		final String p = "col";

		// Resultado
		final LocalDate n = LocalDate.of(2018, Month.APRIL, 10);
		final Message m = new FormattedMessage("La fecha ingresada '{}' no tiene un formato ISO válido", f);

		// Petición
		final LambdaRequest r = new LambdaRequest(f, d, p);

		// Prueba
		Assert.assertEquals(
			"La fecha esperada no coincide",
			m.getFormattedMessage(),
			lh.handleRequest(r, null).getResultado()
		);
	}

	@Test
	public void testHandlerErrorPais() {
		// Datos de entrada
		final String f = "2018-04-01";
		final int d = 5;
		final String p = "pan";

		// Resultado
		final Message m = new FormattedMessage("País '{}' no soportado", p);

		// Petición
		final LambdaRequest r = new LambdaRequest(f, d, p);

		// Prueba
		Assert.assertEquals(
			"La fecha esperada no coincide",
			m.getFormattedMessage(),
			lh.handleRequest(r, null).getResultado()
		);
	}

}
