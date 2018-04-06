package com.ramirezblauvelt.democi.utils;

import com.ramirezblauvelt.democi.beans.Festivo;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SumarFestivos {

	/** Colección para almacenar los festivos por pais */
	private static final ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>> festivosGlobales = new ConcurrentHashMap<>();

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
		if(!festivosGlobales.containsKey(pais) || !festivosGlobales.get(pais).containsKey(year)) {
			// Consulta los festivos para el país y el año
			final Set<LocalDate> festivos = ConsultarFestivos.getFestivosAno(pais, year)
				.parallelStream()
				.map(Festivo::getDate)
				.collect(Collectors.toSet())
			;

			// Carga los festivos
			festivosGlobales.putIfAbsent(pais, new ConcurrentHashMap<>());
			festivosGlobales.get(pais).putIfAbsent(year, festivos);
		}

		// Verifica
		return festivosGlobales.get(pais).get(year).contains(fecha);
	}

}