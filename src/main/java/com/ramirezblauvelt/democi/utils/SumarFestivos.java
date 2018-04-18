package com.ramirezblauvelt.democi.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramirezblauvelt.democi.beans.Festivo;
import com.ramirezblauvelt.democi.beans.PaisSoportado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
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

	/** Archivo local para persistir los festivos */
	private static final Path ARCHIVO_FESTIVOS = Paths.get("festivos.json");

	/** Colección para almacenar los festivos por pais */
	private static final ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>> FESTIVOS_GLOBALES = new ConcurrentHashMap<>();

	/** Tabla de países soportados */
	private static final ConcurrentHashMap<String, PaisSoportado> PAISES_SOPORTADOS = new ConcurrentHashMap<>();

	static {

		try {
			// Refresca el archivo cada día
			if (ARCHIVO_FESTIVOS.toFile().exists() && !Files.getLastModifiedTime(ARCHIVO_FESTIVOS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1).isBefore(LocalDateTime.now())) {

				// Interprete
				final Gson gson = new Gson();
				final Type tipoListaFestivos = new TypeToken<ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>>>(){}.getType();

				// Lee el archivo
				final ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>> fg = gson.fromJson(
					Files.newBufferedReader(ARCHIVO_FESTIVOS),
					tipoListaFestivos
				);

				// Carga la variable local
				FESTIVOS_GLOBALES.putAll(fg);

			}

		} catch (IOException ioe) {
			LOGGER.error("Error leyendo el archivo local de festivos " + ARCHIVO_FESTIVOS, ioe);
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
		if(!FESTIVOS_GLOBALES.containsKey(pais) || !FESTIVOS_GLOBALES.get(pais).containsKey(year)) {
			// Consulta los festivos para el país y el año
			final Set<LocalDate> festivos = ConsultarFestivos.getFestivosAno(pais, year)
				.parallelStream()
				.map(Festivo::getDate)
				.collect(Collectors.toSet())
			;

			// Carga los festivos
			FESTIVOS_GLOBALES.putIfAbsent(pais, new ConcurrentHashMap<>());
			FESTIVOS_GLOBALES.get(pais).putIfAbsent(year, festivos);

			// Actualiza la persistencia local
			final Gson gson = new Gson();
			final String json = gson.toJson(FESTIVOS_GLOBALES);
			try {
				Files.write(ARCHIVO_FESTIVOS, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException ioe) {
				LOGGER.error("Error escribiendo el archivo de festivos local", ioe);
			}
		}

		// Verifica
		return FESTIVOS_GLOBALES.get(pais).get(year).contains(fecha);
	}

	/**
	 * Indica si un país en particular está soportado por el servicio
	 * @param pais código del país a verificar
	 * @return true si el país está soportado o false en cualquier otro caso
	 */
	public static boolean isPaisSoportado(String pais) {
		// Carga la variable local
		if(PAISES_SOPORTADOS.isEmpty()) {
			PAISES_SOPORTADOS.putAll(
				ConsultarFestivos.getPaisesSoportados()
					.parallelStream()
					.collect(Collectors.toConcurrentMap(PaisSoportado::getCountryCode, Function.identity()))
			);
		}

		// Verifica
		return PAISES_SOPORTADOS.containsKey(pais);
	}

}
