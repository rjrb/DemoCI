package com.ramirezblauvelt.democi.test;

import com.ramirezblauvelt.democi.beans.Festivo;
import com.ramirezblauvelt.democi.utils.ConsultarFestivos;
import com.ramirezblauvelt.democi.utils.SumarFestivos;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFechas {

	@Test
	public void testSumarDiasHabilesConFestivos() {
		try {

			Assert.assertEquals(
				"No sumó los días hábiles esperados",
				LocalDate.of(2018, Month.FEBRUARY, 22).toString(),
				SumarFestivos.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.FEBRUARY, 1), 15, "col").toString()
			);

			Assert.assertEquals(
				"No sumó los días hábiles esperados",
				LocalDate.of(2018, Month.FEBRUARY, 23).toString(),
				SumarFestivos.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.FEBRUARY, 3), 15, "col").toString()
			);

			Assert.assertEquals(
				"No sumó los días hábiles esperados",
				LocalDate.of(2018, Month.JANUARY, 23).toString(),
				SumarFestivos.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.JANUARY, 1), 15, "col").toString()
			);

			Assert.assertEquals(
				"No sumó los días hábiles esperados",
				LocalDate.of(2018, Month.MARCH, 26).toString(),
				SumarFestivos.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.MARCH, 4), 15, "col").toString()
			);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSumarDiasHabilesConFestivosCambioEnMes() {
		try {
			Assert.assertEquals(
				"No sumó los días hábiles esperados",
				LocalDate.of(2018, Month.APRIL, 11).toString(),
				SumarFestivos.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.MARCH, 16), 15, "col").toString()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSumarDiasHabilesConFestivosCambioEnAno() {
		try {
			Assert.assertEquals(
				"No sumó los días hábiles esperados",
				LocalDate.of(2019, Month.JANUARY, 9).toString(),
				SumarFestivos.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.DECEMBER, 22), 10, "col").toString()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSumarDiasHabilesConFestivosNegativoCambioMes() {
		try {
			Assert.assertEquals(
				"No sumó los días hábiles esperados",
				LocalDate.of(2018, Month.MARCH, 21).toString(),
				SumarFestivos.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.APRIL, 6), -10, "col").toString()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSumarDiasHabilesConFestivosNegativoCambioAnoColombia() {
		Assert.assertEquals(
			"No sumó los días hábiles esperados",
			LocalDate.of(2018, Month.DECEMBER, 20).toString(),
			SumarFestivos.sumarDiasHabilesConFestivosColombia(LocalDate.of(2019, Month.JANUARY, 10), -12).toString()
		);
	}

	@Test
	public void testSumarDiasHabilesConFestivosColombiaCero() {
		Assert.assertEquals(
			"No sumó los días hábiles esperados",
			LocalDate.of(2018, Month.DECEMBER, 20).toString(),
			SumarFestivos.sumarDiasHabilesConFestivosColombia(LocalDate.of(2018, Month.DECEMBER, 20), 0).toString()
		);
	}

	@Test
	public void testIsFinDeSemana() {
		Assert.assertTrue("No detectó el fin de semana", SumarFestivos.isFinDeSemana(LocalDate.of(2018, 3, 24)));
		Assert.assertTrue("No detectó el fin de semana", SumarFestivos.isFinDeSemana(LocalDate.of(2018, 3, 25)));
		Assert.assertFalse("Falso positivo de fin de semana", SumarFestivos.isFinDeSemana(LocalDate.of(2018, 3, 19)));
		Assert.assertFalse("Falso positivo de fin de semana", SumarFestivos.isFinDeSemana(LocalDate.of(2018, 3, 20)));
		Assert.assertFalse("Falso positivo de fin de semana", SumarFestivos.isFinDeSemana(LocalDate.of(2018, 3, 21)));
		Assert.assertFalse("Falso positivo de fin de semana", SumarFestivos.isFinDeSemana(LocalDate.of(2018, 3, 22)));
		Assert.assertFalse("Falso positivo de fin de semana", SumarFestivos.isFinDeSemana(LocalDate.of(2018, 3, 23)));
	}

	@Test
	public void testIsFestivo() {
		Assert.assertTrue(
			"No detectó el festivo",
			SumarFestivos.isFestivo(LocalDate.of(2018, Month.MARCH, 30), "col")
		);

		Assert.assertTrue(
			"No detectó el festivo",
			SumarFestivos.isFestivo(LocalDate.of(2019, Month.JANUARY, 1), "col")
		);

		Assert.assertFalse(
			"Falso positivo de festivo",
			SumarFestivos.isFestivo(LocalDate.of(2018, Month.MARCH, 23), "col")
		);
	}

	@Test
	public void testIsPaisSoportado() {
		// Colombia está soportado
		Assert.assertTrue(
			"No aceptó país soportado",
			ConsultarFestivos.isPaisSoportado("col")
		);

		// USA está soportado
		Assert.assertTrue(
			"No aceptó país soportado",
			ConsultarFestivos.isPaisSoportado("usa")
		);

		// Panamá NO está soportado
		Assert.assertFalse(
			"Falso positivo",
			ConsultarFestivos.isPaisSoportado("pan")
		);
	}

	@Test
	public void testGetFestivosAno() {
		// Festivos de Colombia en 2018
		final List<LocalDate> festivosColombia2018 = Stream.of(
				"2018-01-01",
				"2018-01-08",
				"2018-03-19",
				"2018-03-29",
				"2018-03-30",
				"2018-05-01",
				"2018-05-14",
				"2018-06-04",
				"2018-06-11",
				"2018-07-02",
				"2018-07-20",
				"2018-08-07",
				"2018-08-20",
				"2018-10-15",
				"2018-11-05",
				"2018-11-12",
				"2018-12-25"
			)
			.map(LocalDate::parse)
			.collect(Collectors.toList())
		;

		// Consulta el servivio
		final List<LocalDate> festivosServicioColombia2018 = ConsultarFestivos.getFestivosAno("col", 2018)
			.parallelStream()
				.map(Festivo::getDate)
				.filter(d -> !SumarFestivos.isFinDeSemana(d))
				.sorted()
				.collect(Collectors.toList())
		;

		// Compara
		Assert.assertEquals(
			"Los conjuntos de festivos no son iguales",
			festivosColombia2018,
			festivosServicioColombia2018
		);
	}

}
