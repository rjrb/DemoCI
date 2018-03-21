package com.ramirezblauvelt.democi.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramirezblauvelt.democi.beans.Festivo;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

public class ConsultarFestivos {

	/** URL de la API */
	public static final String URL_API = "http://kayaposoft.com/enrico";

	/** URL relativa de la API */
	public static final String API_PATH = "json/v2.0";

	/**
	 * Procedimiento para obtener, desde un servicio web, los festivos de un país para un año en particular
	 * @param pais
	 * @param ano
	 * @return
	 * @throws RuntimeException
	 */
	public static List<Festivo> getFestivosAno(String pais, int ano) throws RuntimeException {

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
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " ---> " + response.getStatusInfo());
			}

			// Convierte la respuesta
			final Gson gson = new Gson();
			final Type tipoListaFestivos = new TypeToken<List<Festivo>>(){}.getType();
			final List<Festivo> festivos = gson.fromJson(response.readEntity(String.class), tipoListaFestivos);

			// Entrega el dato
			return festivos;
		}
	}

}
