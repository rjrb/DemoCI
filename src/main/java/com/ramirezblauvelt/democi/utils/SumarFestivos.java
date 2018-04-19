package com.ramirezblauvelt.democi.utils;

import com.google.gson.Gson;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;
import com.ramirezblauvelt.democi.beans.Festivo;
import com.ramirezblauvelt.democi.beans.PaisSoportado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SumarFestivos {

	/** Logger */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Archivo local para persistir los datos del servicio web */
	private static final Path ARCHIVO_PERSISTENCIA = Paths.get("persistencia.json");

	/** Contenedor de objetos persistidos */
	private static ContenedorPersistencia contenedorPersistencia = new ContenedorPersistencia();

	static {

		try {

			// Refresca el archivo cada día
			if (ARCHIVO_PERSISTENCIA.toFile().exists() && !Files.getLastModifiedTime(ARCHIVO_PERSISTENCIA).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1).isBefore(LocalDateTime.now())) {

				// Interprete
				final Gson gson = new Gson();

				// Lee el archivo
				contenedorPersistencia = gson.fromJson(
					Files.newBufferedReader(ARCHIVO_PERSISTENCIA),
					ContenedorPersistencia.class
				);

			}

		} catch (IOException ioe) {
			LOGGER.error("Error leyendo el archivo local de festivos " + ARCHIVO_PERSISTENCIA, ioe);
		}


	}

	private SumarFestivos() {

	}

	/**
	 * Procedimiento que suma a una fecha inicial, el número de días hábiles indicados, teniendo
	 * en cuenta sábados y domingos como días no laborales, y los festivos del país en cuestión.
	 * Los datos de festivos son obtenidos a partir de un servicio web externo
	 * @param fechaInicial fecha inicial a la cual se le suman los días
	 * @param diasHabilesSumar días hábiles a sumar a la fecha inicial
	 * @param pais código del país para el cual se buscarán los festivos en el servicio web
	 * @return la fecha resultante de sumar a la fecha inicial, la cantidad de días hábiles indicados
	 */
	public static LocalDate sumarDiasHabilesConFestivos(LocalDate fechaInicial, int diasHabilesSumar, String pais) {
		// Operador
		final int paso = Integer.signum(diasHabilesSumar);

		// Días absolutos a sumar
		int dias = Math.abs(diasHabilesSumar);

		// Encuentra los días
		LocalDate referencia = LocalDate.from(fechaInicial);
		while(dias > 0) {
			// Suma un día
			referencia = referencia.plusDays(paso);

			// Valida si el día es hábil
			if(!isFinDeSemana(referencia) && !isFestivo(referencia, pais)) {
				dias--;
			}
		}

		// Entrega la fecha
		return referencia;
	}

	/**
	 * Procedimiento que suma a la fecha inicial, la cantidad de días hábiles en Colombia
	 * @param fechaInicial fecha inicial a la cual se le suman los días
	 * @param diasHabilesSumar días hábiles a sumar a la fecha inicial
	 * @return la fecha resultante de sumar a la fecha inicial, la cantidad de días hábiles indicados, en Colombia
	 */
	public static LocalDate sumarDiasHabilesConFestivosColombia(LocalDate fechaInicial, int diasHabilesSumar) {
		return sumarDiasHabilesConFestivos(fechaInicial, diasHabilesSumar, "col");
	}

	/**
	 * Procedimiento que indica si una fecha dada, es un día del fin de semana. Por fin de semana, se entiende sábado
	 * y domingo
	 * @param fecha fecha a validar
	 * @return true si es un fin de semana o false si es cualquier otro día
	 */
	public static boolean isFinDeSemana(LocalDate fecha) {
		return fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY;
	}

	/**
	 * Procedimiento que indica si una fecha dada, es festivo en un país
	 * @param fecha fecha a validar
	 * @param pais código del país para el cual se buscarán los festivos en el servicio web
	 * @return true si es un festivo o false si es cualquier otro día
	 */
	public static boolean isFestivo(LocalDate fecha, String pais) {
		// Consulta los festivos (si no están en caché)
		final int year = fecha.getYear();
		if(!contenedorPersistencia.getFestivosGlobales().containsKey(pais) || !contenedorPersistencia.getFestivosGlobales().get(pais).containsKey(year)) {
			// Consulta los festivos para el país y el año
			final Set<LocalDate> festivos = ConsultarFestivos.getFestivosAno(pais, year)
				.parallelStream()
				.map(Festivo::getDate)
				.collect(Collectors.toSet())
			;

			// Carga los festivos
			contenedorPersistencia.getFestivosGlobales().putIfAbsent(pais, new ConcurrentHashMap<>());
			contenedorPersistencia.getFestivosGlobales().get(pais).putIfAbsent(year, festivos);

			// Actualiza la persistencia local
			escribirPersistencia();
		}

		// Verifica
		return contenedorPersistencia.getFestivosGlobales().get(pais).get(year).contains(fecha);
	}

	/**
	 * Indica si un país en particular está soportado por el servicio
	 * @param pais código del país a verificar
	 * @return true si el país está soportado o false en cualquier otro caso
	 */
	public static boolean isPaisSoportado(String pais) {
		// Carga la variable local
		if(contenedorPersistencia.getPaisesSoportados().isEmpty()) {
			// Carga los países soportados
			contenedorPersistencia.getPaisesSoportados().putAll(
				ConsultarFestivos.getPaisesSoportados()
					.parallelStream()
					.collect(Collectors.toConcurrentMap(PaisSoportado::getCountryCode, Function.identity()))
			);

			// Actualiza la persistencia local
			escribirPersistencia();
		}

		// Verifica
		return contenedorPersistencia.getPaisesSoportados().containsKey(pais);
	}

	/**
	 * Escribe la persistencia local
	 */
	private static void escribirPersistencia() {
		final Gson gson = new Gson();
		final String json = gson.toJson(contenedorPersistencia);
		try {
			Files.write(ARCHIVO_PERSISTENCIA, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException ioe) {
			LOGGER.error("Error escribiendo el archivo de persistencia local", ioe);
		}
	}

}
