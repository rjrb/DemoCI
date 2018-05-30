package com.ramirezblauvelt.democi.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ramirezblauvelt.democi.beans.LambdaRequest;
import com.ramirezblauvelt.democi.utils.SumarFestivos;
import com.ramirezblauvelt.democi.utils.Utilidades;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LambdaHandler implements RequestHandler<LambdaRequest, String> {

	/* Importa la configuración del logger */
	static {
		System.setProperty("log4j2.configurationFile", "com/ramirezblauvelt/democi/log4j2.xml");
	}

	/* Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();


	/**
	 * Punto de entrada para la función lambda
	 *
	 * @param input Bean de entrada (payload) con los datos necesarios para la función
	 * @param context AWS Lambda context
	 * @return la fecha que resulta de sumar a la fecha inicial, en el país indicado, la cantidad de días hábiles indicados
	 */
	@Override public String handleRequest(LambdaRequest input, Context context) {
		// Elimina el appender de consola
		Utilidades.eliminarAppender("Console");

		// Parámetros de entrada
		final String fechaInicial = input.getFechaInicial();
		final int dias = input.getDiasHabilesSumar();
		final String pais = input.getPais();

		// Registra los parámetros de entrda
		LOGGER.info("Petición recibida el {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		LOGGER.info("Fecha inicial: {}", fechaInicial);
		LOGGER.info("Días hábiles a sumar: {}", dias);
		LOGGER.info("País: {}", pais);

		// Convierte la fecha (si no la puede convertir, el formato no es válido)
		LocalDate fecha = Utilidades.getFecha(fechaInicial);
		if(fecha == null) {
			final Message m = new FormattedMessage("La fecha ingresada '{}' no tiene un formato ISO válido", fechaInicial);
			LOGGER.error(m);
			return m.getFormattedMessage();
		}

		// Obtiene el nombre del país (si no lo encuentra, es porque no está soportado)
		final String nombrePais = SumarFestivos.getNombrePais(pais);
		if(nombrePais == null) {
			final Message m = new FormattedMessage("País '{}' no soportado", pais);
			LOGGER.error(m);
			return m.getFormattedMessage();
		}

		// Obtiene el resultado
		final LocalDate nuevaFecha = SumarFestivos.sumarDiasHabilesConFestivos(
			fecha,
			dias,
			pais
		);

		// Mensaje de salida
		final Message resultado = new FormattedMessage(
			"El resultado de sumar '{}' días hábiles en '{}' a la fecha '{}' es: '{}'",
			dias,
			nombrePais,
			fechaInicial,
			nuevaFecha
		);

		// Registra el resultado
		LOGGER.info(resultado);

		// Retorna el resultado
		return resultado.getFormattedMessage();
	}

}
