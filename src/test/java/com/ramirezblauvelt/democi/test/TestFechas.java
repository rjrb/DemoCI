package com.ramirezblauvelt.democi.test;

import com.ramirezblauvelt.democi.Fechas;
import com.ramirezblauvelt.democi.beans.Festivo;
import com.ramirezblauvelt.democi.utils.ConsultarFestivos;
import com.ramirezblauvelt.democi.utils.SumarFestivos;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFechas {

	/* Log de eventos */
	static {
		System.setProperty("log4j2.configurationFile", "com/ramirezblauvelt/democi/log4j2.xml");
	}


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
			SumarFestivos.isPaisSoportado("col")
		);

		// USA está soportado
		Assert.assertTrue(
			"No aceptó país soportado",
			SumarFestivos.isPaisSoportado("usa")
		);

		// Panamá NO está soportado
		Assert.assertFalse(
			"Falso positivo",
			SumarFestivos.isPaisSoportado("pan")
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


	/** Capturador de consola */
	private static ByteArrayOutputStream out = new ByteArrayOutputStream();

	@BeforeClass
	public static void entorno() {
		System.setOut(new PrintStream(out));
	}

	@Test
	public void testSinParametro() {
		Fechas.procesar(new String[] {});
		Assert.assertThat(out.toString(), StringContains.containsString("No especificó una acción. Se espera uno de los siguientes parámetros"));
	}

	@Test
	public void testAccionInvalida() {
		Fechas.procesar(new String[] {"no_valido"});
		Assert.assertThat(out.toString(), StringContains.containsString("No especificó una acción válida. Se espera una de las siguientes acciones"));
	}

	@Test
	public void testAccionSfcSinArgumentos() {
		Fechas.procesar(new String[] {"sfc"});
		Assert.assertThat(out.toString(), StringContains.containsString("Se esperan 2 argumentos para la operación sfc y se recibieron 0"));
	}

	@Test
	public void testAccionSfSinArgumentos() {
		Fechas.procesar(new String[] {"sf"});
		Assert.assertThat(out.toString(), StringContains.containsString("Se esperan 3 argumentos para la operación sf y se recibieron 0"));
	}

	@Test
	public void testAccionSfcSinDias() {
		Fechas.procesar(new String[] {"sfc", "2018-03-23"});
		Assert.assertThat(out.toString(), StringContains.containsString("Se esperan 2 argumentos para la operación sfc y se recibieron 1"));
	}

	@Test
	public void testAccionSfcConDiasInvalidos() {
		Fechas.procesar(new String[] {"sfc", "2018-03-23", "zfdfsd"});
		Assert.assertThat(out.toString(), StringContains.containsString("No se pudo convertir la cantidad de días 'zfdfsd'"));
	}

	@Test
	public void testAccionSfcConFechaInvalida() {
		Fechas.procesar(new String[] {"sfc", "20180323", "zfdfsd"});
		Assert.assertThat(out.toString(), StringContains.containsString("No se pudo convertir la fecha '20180323'"));
	}

	@Test
	public void testSumarFestivosColombia() {
		// Datos
		final List<DatosPrueba> datos = new ArrayList<>();
		datos.add(new DatosPrueba("col", LocalDate.of(2018, Month.MARCH, 23), 10, LocalDate.of(2018, Month.APRIL, 10)));
		datos.add(new DatosPrueba("col", LocalDate.of(2018, Month.APRIL, 30), 5, LocalDate.of(2018, Month.MAY, 8)));
		datos.add(new DatosPrueba("col", LocalDate.of(2017, Month.DECEMBER, 24), 15, LocalDate.of(2018, Month.JANUARY, 17)));

		// Plantilla de resultado correcto
		final String resultadoEsperado = "El resultado de sumar '%s' días hábiles en '%s' a la fecha '%s' es: '%s'";

		// Ejecuta las pruebas
		for(DatosPrueba dp : datos) {
			Fechas.procesar(new String[]{"sfc", dp.getFechaReferencia().toString(), String.valueOf(dp.getDiasHabiles())});
			Assert.assertThat(
					out.toString(),
					StringContains.containsString(
							String.format(
									resultadoEsperado,
									dp.getDiasHabiles(),
									dp.getPais(),
									dp.getFechaReferencia(),
									dp.getFechaEsperada()
							)
					)
			);
		}
	}

	@Test
	public void testSumarFestivosPais() {
		// Datos
		final List<DatosPrueba> datos = new ArrayList<>();
		datos.add(new DatosPrueba("col", LocalDate.of(2018, Month.MARCH, 23), 10, LocalDate.of(2018, Month.APRIL, 10)));
		datos.add(new DatosPrueba("usa", LocalDate.of(2017, Month.DECEMBER, 24), 15, LocalDate.of(2018, Month.JANUARY, 12)));

		// Plantilla de resultado correcto
		final String resultadoEsperado = "El resultado de sumar '%s' días hábiles en '%s' a la fecha '%s' es: '%s'";

		// Ejecuta las pruebas
		for(DatosPrueba dp : datos) {
			Fechas.procesar(new String[]{"sf", dp.getFechaReferencia().toString(), String.valueOf(dp.getDiasHabiles()), dp.getPais()});
			Assert.assertThat(
					out.toString(),
					StringContains.containsString(
							String.format(
									resultadoEsperado,
									dp.getDiasHabiles(),
									dp.getPais(),
									dp.getFechaReferencia(),
									dp.getFechaEsperada()
							)
					)
			);
		}
	}


	/**
	 * Bean para datos de prueba
	 */
	static class DatosPrueba {

		private String pais;
		private LocalDate fechaReferencia;
		private int diasHabiles;
		private LocalDate fechaEsperada;

		DatosPrueba(String pais, LocalDate fechaReferencia, int diasHabiles, LocalDate fechaEsperada) {
			this.pais = pais;
			this.fechaReferencia = fechaReferencia;
			this.diasHabiles = diasHabiles;
			this.fechaEsperada = fechaEsperada;
		}

		String getPais() {
			return pais;
		}

		LocalDate getFechaReferencia() {
			return fechaReferencia;
		}

		int getDiasHabiles() {
			return diasHabiles;
		}

		LocalDate getFechaEsperada() {
			return fechaEsperada;
		}

	}

}
