package com.ramirezblauvelt.democi.test;

import com.ramirezblauvelt.democi.Fechas;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;
import java.time.Month;

public class TestFechas {

	@Test
	public void testSumarDiasHabilesConFestivos() {
		try {

			Assert.assertEquals(
					"No sumó los días hábiles esperados",
					LocalDate.of(2018, Month.FEBRUARY, 22).toString(),
					Fechas.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.FEBRUARY, 1), 15, "col").toString()
			);

			Assert.assertEquals(
					"No sumó los días hábiles esperados",
					LocalDate.of(2018, Month.FEBRUARY, 23).toString(),
					Fechas.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.FEBRUARY, 3), 15, "col").toString()
			);

			Assert.assertEquals(
					"No sumó los días hábiles esperados",
					LocalDate.of(2018, Month.JANUARY, 23).toString(),
					Fechas.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.JANUARY, 1), 15, "col").toString()
			);

			Assert.assertEquals(
					"No sumó los días hábiles esperados",
					LocalDate.of(2018, Month.MARCH, 26).toString(),
					Fechas.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.MARCH, 4), 15, "col").toString()
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
					Fechas.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.MARCH, 16), 15, "col").toString()
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
					Fechas.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.DECEMBER, 22), 10, "col").toString()
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
					Fechas.sumarDiasHabilesConFestivos(LocalDate.of(2018, Month.APRIL, 6), -10, "col").toString()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
