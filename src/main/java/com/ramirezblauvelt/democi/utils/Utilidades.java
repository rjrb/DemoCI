package com.ramirezblauvelt.democi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Utilidades {

	/** Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();

	private Utilidades() {

	}

	/**
	 * Procedimiento que intenta convertir una fecha en formato ISO aaaa-mm-dd a una instancia de LocalDate
	 * @param fechaComoTexto la fecha como texto. Se espera recibirla en formato ISO
	 * @return la instancia de la fecha si la conversión fue posible, o null en cualquier otro caso
	 */
	public static LocalDate getFecha(String fechaComoTexto) {
		try {
			return LocalDate.parse(fechaComoTexto);
		} catch (DateTimeParseException dtpe) {
			LOGGER.error("No se pudo convertir la fecha '{}'", fechaComoTexto);
			return null;
		}
	}

	/**
	 * Procedimiento que intenta convertir un número de días como texto, en un número entero
	 * @param diasComoTexto los días como un entero
	 * @return la instancia de los días enteros a sumar si la conversión fue posible, o null en cualquier otro caso
	 */
	public static Integer getDiasHabiles(String diasComoTexto) {
		try {
			return Integer.parseInt(diasComoTexto);
		} catch (NumberFormatException nfe) {
			LOGGER.error("No se pudo convertir la cantidad de días '{}'", diasComoTexto);
			return null;
		}
	}

}
