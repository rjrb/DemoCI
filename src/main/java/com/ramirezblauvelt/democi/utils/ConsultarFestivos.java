package com.ramirezblauvelt.democi.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramirezblauvelt.democi.beans.ErrorServicio;
import com.ramirezblauvelt.democi.beans.Festivo;
import com.ramirezblauvelt.democi.beans.PaisSoportado;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConsultarFestivos {

	/** URL de la API */
	private static final String URL_API = "http://kayaposoft.com/enrico";

	/** URL relativa de la API */
	private static final String API_PATH = "json/v2.0";

	/** Tabla de países soportados */
	private static final ConcurrentHashMap<String, PaisSoportado> PAISES_SOPORTADOS = new ConcurrentHashMap<>();

	private ConsultarFestivos() {

	}

	/**
	 * Procedimiento para obtener, desde un servicio web, los festivos de un país para un año en particular
	 * @param pais código del país para el cual se buscan los festivos, de los soportados por el servicio
	 * @param ano año para el cual se consultan los festivos
	 * @return el conjunto de festivos públicos para el país y el año especificados
	 * @throws RuntimeException si no se puedo consultar el servicio o falla la conversión de datos
	 */
	public static Set<Festivo> getFestivosAno(String pais, int ano) {

		// Consume el servicio web
		try (
			Response response = ClientBuilder.newClient()
				.target(URL_API)
				.path(API_PATH)
				.queryParam("action", "getHolidaysForYear")
				.queryParam("country", pais)
				.queryParam("year", ano)
				.queryParam("holidayType", "public_holiday")
				.request(MediaType.APPLICATION_JSON)
				.get()
		) {
			// Lee la respuesta
			if(response.getStatus() != 200) {
				throw new IllegalStateException("Falló : Código de error HTTP : " + response.getStatus() + " ---> " + response.getStatusInfo());
			}

			// Lee la respuesta
			final String respuesta = response.readEntity(String.class);

			// Convierte la respuesta
			final Gson gson = new Gson();

			// Valida si hubo error
			if(respuesta.contains("error")) {
				final ErrorServicio error = gson.fromJson(respuesta, ErrorServicio.class);
				throw new IllegalArgumentException(error.getError());
			}

			// Entrega el dato
			final Type tipoListaFestivos = new TypeToken<Set<Festivo>>(){}.getType();
			return gson.fromJson(respuesta, tipoListaFestivos);
		}
	}

	/**
	 * Procedimiento para obtener, desde un servicio web, los países soportados para consultar los festivos
	 * @return el conjunto de países soportados por el servicio Web
	 */
	private static Set<PaisSoportado> getPaisesSoportados() {

		// Consume el servicio web
		try (
			Response response = ClientBuilder.newClient()
				.target(URL_API)
				.path(API_PATH)
				.queryParam("action", "getSupportedCountries")
				.request(MediaType.APPLICATION_JSON)
				.get()
		) {
			// Lee la respuesta
			if(response.getStatus() != 200) {
				throw new IllegalStateException("Failed : HTTP error code : " + response.getStatus() + " ---> " + response.getStatusInfo());
			}

			// Convierte la respuesta
			final Gson gson = new Gson();
			final Type tipoListaPaisesSoportados = new TypeToken<Set<PaisSoportado>>(){}.getType();

			// Entrega el dato
			return gson.fromJson(response.readEntity(String.class), tipoListaPaisesSoportados);
		}
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
				getPaisesSoportados()
					.parallelStream()
					.collect(Collectors.toConcurrentMap(PaisSoportado::getCountryCode, Function.identity()))
			);
		}

		// Verifica
		return PAISES_SOPORTADOS.containsKey(pais);
	}

}
