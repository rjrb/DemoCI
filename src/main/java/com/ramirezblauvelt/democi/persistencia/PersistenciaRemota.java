package com.ramirezblauvelt.democi.persistencia;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramirezblauvelt.democi.aws.dynamo.AdicionarFestivos;
import com.ramirezblauvelt.democi.aws.dynamo.ConsultarFestivos;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;
import com.ramirezblauvelt.democi.beans.PaisSoportado;
import com.ramirezblauvelt.democi.beans.PaisSoportadoAPI;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersistenciaRemota implements Persistencia {

	/** URL de la API */
	private static final String URL_API = "https://v30stkwesf.execute-api.us-east-1.amazonaws.com/v1";

	/** URL relativa de la API */
	private static final String API_PATH = "paisessoportados";


	@Override public void setPersistencia(ContenedorPersistencia contenedorPersistencia) {
		AdicionarFestivos.adicionarFestivos(contenedorPersistencia);
	}

	@Override public ContenedorPersistencia getPersistencia() {
		// Obtiene los festivos
		final ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> festivosGlobales = ConsultarFestivos.listarTodosFestivos();

		// Obtiene los países soportados
		ConcurrentMap<String, PaisSoportado> paisesSoportados;

		// Consume el servicio web
		try (
			Response response = ClientBuilder.newClient()
                .target(URL_API)
                .path(API_PATH)
                .request(MediaType.APPLICATION_JSON)
                .get()
		) {
			// Lee la respuesta
			if(response.getStatus() != 200) {
				throw new IllegalStateException("Failed : HTTP error code : " + response.getStatus() + " ---> " + response.getStatusInfo());
			}

			// Convierte la respuesta
			final Gson gson = new Gson();
			final Type tipoListaPaisesSoportados = new TypeToken<Set<PaisSoportadoAPI>>(){}.getType();

			// Entrega el dato
			final Set<PaisSoportadoAPI> paises = gson.fromJson(response.readEntity(String.class), tipoListaPaisesSoportados);

			// Obtiene la estructura
			paisesSoportados = paises.parallelStream()
				.map(p -> {
					final PaisSoportado ps = new PaisSoportado();
					ps.setCountryCode(p.getCodigo());
					ps.setFullName(p.getNombre());
					return ps;
				})
				.collect(Collectors.toConcurrentMap(PaisSoportado::getCountryCode, Function.identity()))
			;
		}

		// Crea el contenedor
		final ContenedorPersistencia contenedorPersistencia = new ContenedorPersistencia();
		contenedorPersistencia.setFestivosGlobales(festivosGlobales);
		contenedorPersistencia.setPaisesSoportados(paisesSoportados);

		// Entrega el contenedor
		return contenedorPersistencia;
	}
}
