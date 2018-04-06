package com.ramirezblauvelt.democi;

import com.ramirezblauvelt.democi.utils.ConsultarFestivos;
import com.ramirezblauvelt.democi.utils.SumarFestivos;
import com.ramirezblauvelt.democi.utils.Utilidades;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Clase con funciones para fechas
 */
public class Fechas {

	/* Log de eventos */
	static {
		System.setProperty("log4j2.configurationFile", "com/ramirezblauvelt/democi/log4j2.xml");
	}
	private static final Logger LOGGER = LogManager.getLogger();

	/** Argumentos esperados */
	private static final Map<String, String> EXPECTED_ARGS =
		Stream.of(
			new AbstractMap.SimpleEntry<>("sf",  "fechaInicial(aaaa-mm-dd) día pais"),
			new AbstractMap.SimpleEntry<>("sfc", "fechaInicial(aaaa-mm-dd) día")
		)
		.collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
	;

	/**
	 * Método de entrada
	 * @param args argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		// Integridad
		if(args.length == 0) {
			LOGGER.error("No especificó una acción. Se espera uno de los siguientes parámetros:");
			EXPECTED_ARGS.forEach(Fechas::accept);
			System.exit(1);
		}

		// Lee el argumento
		final String operacion = args[0];
		if(!EXPECTED_ARGS.containsKey(operacion)) {
			LOGGER.error("No especificó una acción válida. Se espera una de las siguientes acciones:");
			EXPECTED_ARGS.keySet().forEach(LOGGER::error);
			System.exit(1);
		}

		// Lee la cantidad de argumentos entregados
		final int argumentos = args.length - 1;

		// Lee la cantidad de argumentos esperados
		final int argumentosEsperados = EXPECTED_ARGS.get(operacion).split("\\s").length;
		if(argumentos != argumentosEsperados) {
			LOGGER.error("Se esperan {} argumentos para la operación {} y se recibieron {}", argumentosEsperados, operacion, argumentos);
			LOGGER.error("Modo de operación: {} {}", operacion, EXPECTED_ARGS.get(operacion));
			System.exit(1);
		}

		// Lee la fecha
		final LocalDate fechaInicial = Utilidades.getFecha(args[1]);
		if(fechaInicial == null) {
			LOGGER.error("La fecha ingresada '{}' no tiene un formato ISO válido", args[1]);
			System.exit(1);
		}

		// Lee la cantidad de días a sumar
		final Integer diasHabiles = Utilidades.getDiasHabiles(args[2]);
		if(diasHabiles == null) {
			LOGGER.error("Los días ingresados '{}' no son un número entero válido", args[2]);
			System.exit(1);
		}

		// Si se espera el país
		String pais = null;
		if(argumentosEsperados == 3) {
			// Lee el país
			pais = args[3];

			// Valida si el país está soportado por el servicio
			if(!ConsultarFestivos.isPaisSoportado(pais)) {
				LOGGER.error("País '{}' no soportado", pais);
				System.exit(0);
			}
		}

		// Ejecuta la acción
		String resultado;
		if(operacion.equals("sf")) {
			resultado = sumarFestivos(pais, fechaInicial, diasHabiles);
		} else {
			resultado = sumarFestivosColombia(fechaInicial, diasHabiles);
		}

		// Muestra el resultado obtenido
		LOGGER.info(resultado);

		// Proceso exitoso
		System.exit(0);
	}

	/**
	 * Procedimiento que suma los días hábiles a la fecha inicial, en el país indicado
	 * @param pais país del cual se tomarán los festivos
	 * @param fechaInicial fecha inicial a partir de la cual se hace la suma
	 * @param dias días hábiles a sumar
	 * @return la fecha resultante de sumar la cantidad de días hábiles a la fecha inicial, en el país indicado
	 */
	private static String sumarFestivos(String pais, LocalDate fechaInicial, int dias) {
		// Obtiene la fecha
		final LocalDate nuevaFecha = SumarFestivos.sumarDiasHabilesConFestivos(
			fechaInicial,
			dias,
			pais
		);

		// Mensaje
		return "El resultado de sumar '" + dias + "' días hábiles en '" + pais + "' a la fecha '" + fechaInicial + "' es: '" + nuevaFecha + "'";
	}

	/**
	 * Procedimiento que suma los días hábiles a la fecha inicial, en Colombia
	 * @param fechaInicial fecha inicial a partir de la cual se hace la suma
	 * @param dias días hábiles a sumar
	 * @return la fecha resultante de sumar la cantidad de días hábiles a la fecha inicial, en Colombia
	 */
	private static String sumarFestivosColombia(LocalDate fechaInicial, int dias) {
		return sumarFestivos("col", fechaInicial, dias);
	}

	/**
	 * BiFunction
	 * @param parametro nombre del parámetro aceptado
	 * @param argumentos lista de argumentos esperados para ése parámetro
	 */
	private static void accept(String parametro, String argumentos) {
		LOGGER.error(parametro + " " + argumentos);
	}
}
