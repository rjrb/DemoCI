package com.ramirezblauvelt.democi;

import com.ramirezblauvelt.democi.beans.Festivo;
import com.ramirezblauvelt.democi.utils.ConsultarFestivos;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Clase con funciones para fechas
 */
public class Fechas {

	/**
	 * Método de entrada
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.MARCH, 21), 1, "col"));
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
		final int operador = diasHabilesSumar >= 0 ? 1 : -1;

		// Días absolutos a sumar
		int dias = Math.abs(diasHabilesSumar);

		// Encuentra los días
		LocalDate referencia = LocalDate.from(fechaInicial);
		int year = -1;
		final Set<LocalDate> festivos = new TreeSet<>();
		while(dias > 0) {
			// Suma un día
			referencia = referencia.plusDays(operador);

			// Obtiene los festivos del año
			if(year != referencia.getYear()) {
				year = referencia.getYear();
				festivos.addAll(
					ConsultarFestivos.getFestivosAno(pais, year)
						.parallelStream()
						.map(Festivo::getDate)
						.collect(Collectors.toSet())
				);
			}

			// Valida si el día es hábil
			if(
				referencia.getDayOfWeek() != DayOfWeek.SATURDAY
				&& referencia.getDayOfWeek() != DayOfWeek.SUNDAY
				&& !festivos.contains(referencia)
			) {
				dias--;
			}
		}

		// Entrega la fecha
		return referencia;
	}

}
