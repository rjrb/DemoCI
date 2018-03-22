package com.ramirezblauvelt.democi;

import com.ramirezblauvelt.democi.beans.Festivo;
import com.ramirezblauvelt.democi.utils.ConsultarFestivos;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Clase con funciones para fechas
 */
public class Fechas {

	/** Colección para almacenar los festivos por pais */
	private static final ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>> festivosGlobales = new ConcurrentHashMap<>();

	/**
	 * Método de entrada
	 * @param args argumentos de la línea de comandos
	 */
	public static void main(String[] args) {
		System.out.println(
				sumarDiasHabilesConFestivos(
						LocalDate.of(2018, Month.MARCH, 21),
						1,
						"col"
				)
		);
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
		int year = -1;
		while(dias > 0) {
			// Suma un día
			referencia = referencia.plusDays(paso);

			// Obtiene los festivos del año
			if(year != referencia.getYear()) {
				year = referencia.getYear();
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
			}

			// Valida si el día es hábil
			if(
				referencia.getDayOfWeek() != DayOfWeek.SATURDAY
				&& referencia.getDayOfWeek() != DayOfWeek.SUNDAY
				&& !festivosGlobales.get(pais).get(year).contains(referencia)
			) {
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

}
