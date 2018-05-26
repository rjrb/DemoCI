package com.ramirezblauvelt.democi.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ramirezblauvelt.democi.utils.SumarFestivos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LambdaHandler implements RequestHandler<Request, LocalDate> {

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
	@Override
	public LocalDate handleRequest(Request input, Context context) {
		// Parámetros de entrada
		final LocalDate fechaInicial = input.getFechaInicial();
		final int dias = input.getDiasHabilesSumar();
		final String pais = input.getPais();

		// Registra los parámetros de entrda
		LOGGER.info("Petición recibida el {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		LOGGER.info("Fecha inicial: {}", fechaInicial);
		LOGGER.info("Días hábiles a sumar: {}", dias);
		LOGGER.info("País: {}", dias);

		// Obtiene el resultado
		final LocalDate nuevaFecha = SumarFestivos.sumarDiasHabilesConFestivos(
			fechaInicial,
			dias,
			pais
		);

		// Registra el resultado
		LOGGER.info("El resultado de sumar '" + dias + "' días hábiles en '" + pais + "' a la fecha '" + fechaInicial + "' es: '" + nuevaFecha + "'");

		// Retorna el resultado
		return nuevaFecha;
	}

}
