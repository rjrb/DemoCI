package com.ramirezblauvelt.democi.persistencia;

import com.google.gson.Gson;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PersistenciaLocal implements Persistencia {

	/** Archivo local para persistir los datos del servicio web */
	private static final Path PATH_PERSISTENCIA = Paths.get("persistencia.json");

	/* Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();

	@Override public void setPersistencia(ContenedorPersistencia contenedorPersistencia) {
		// Escribe el archivo
		final Gson gson = new Gson();
		final String json = gson.toJson(contenedorPersistencia);
		try {
			Files.write(PATH_PERSISTENCIA, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException ioe) {
			LOGGER.error("Error escribiendo el archivo de persistencia local", ioe);
		}
	}

	@Override public ContenedorPersistencia getPersistencia() {
		// Crea la instancia
		ContenedorPersistencia contenedorPersistencia = new ContenedorPersistencia();

		try {

			// Refresca el archivo cada día
			if (PATH_PERSISTENCIA.toFile().exists()) {

				// Interprete
				final Gson gson = new Gson();

				// Lee el archivo
				contenedorPersistencia = gson.fromJson(
					Files.newBufferedReader(PATH_PERSISTENCIA),
					ContenedorPersistencia.class
				);

			}

		} catch (IOException ioe) {
			LOGGER.error("Error leyendo el archivo local de festivos " + PATH_PERSISTENCIA, ioe);
		}

		// Entrega el contenedor
		return contenedorPersistencia;
	}
}
