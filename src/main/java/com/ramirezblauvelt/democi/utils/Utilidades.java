package com.ramirezblauvelt.democi.utils;

import com.google.gson.Gson;
import com.ramirezblauvelt.democi.aws.ConstantesAWS;
import com.ramirezblauvelt.democi.aws.s3.DescargarArchivo;
import com.ramirezblauvelt.democi.aws.s3.SubirArchivo;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

public class Utilidades implements ConstantesAWS {

	/** Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Bandera que indica si se está trabajando localmente o no */
	private static final boolean LOCAL = Files.isWritable(Paths.get("."));

	/** Archivo local para persistir los datos del servicio web */
	private static final Path PATH_PERSISTENCIA = Paths.get(ARCHIVO_PERSISTENCIA);

	private Utilidades() {

	}

	/**
	 * Procedimiento que intenta convertir una fecha en formato ISO aaaa-mm-dd a una instancia de LocalDate
	 * @param fechaComoTexto la fecha como texto. Se espera recibirla en formato ISO
	 * @return la instancia de la fecha si la conversión fue posible, o null en cualquier otro caso
	 */
	public static LocalDate getFecha(String fechaComoTexto) {
		try {
			return LocalDate.parse(fechaComoTexto);
		} catch (DateTimeParseException dtpe) {
			LOGGER.error("No se pudo convertir la fecha '{}'", fechaComoTexto);
			return null;
		}
	}

	/**
	 * Procedimiento que intenta convertir un número de días como texto, en un número entero
	 * @param diasComoTexto los días como un entero
	 * @return la instancia de los días enteros a sumar si la conversión fue posible, o null en cualquier otro caso
	 */
	public static Integer getDiasHabiles(String diasComoTexto) {
		try {
			return Integer.parseInt(diasComoTexto);
		} catch (NumberFormatException nfe) {
			LOGGER.error("No se pudo convertir la cantidad de días '{}'", diasComoTexto);
			return null;
		}
	}

	/**
	 * Convierte una fecha de archivo a tiempo local
	 * @param fileTime la fecha obtenida de un archivo
	 * @return la fecha en formato local
	 */
	public static LocalDateTime fileTimeToLocalDateTime(FileTime fileTime) {
		return fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * Elimina un appender del logger configurado
	 * @param nombreAppender nombre del appender a eliminar de la configuración del logger
	 */
	public static void eliminarAppender(String nombreAppender) {
		final LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		config.getRootLogger().removeAppender(nombreAppender);
		ctx.updateLoggers();
	}

	/**
	 * Procedimiento que carga la persistencia en memoria (si la hay)
	 * @return entrega la instancia del contenedor
	 */
	public static ContenedorPersistencia getPersistencia() {
		// Crea la instancia
		ContenedorPersistencia contenedorPersistencia = new ContenedorPersistencia();

		// Archivo a persistir
		final Path archivoLocal = LOCAL ? PATH_PERSISTENCIA : Paths.get("/tmp", ARCHIVO_PERSISTENCIA);

		// AWS
		if(!LOCAL) {
			DescargarArchivo.descargarArchivo(archivoLocal);
		}

		// Lee el archivo de persistencia
		try {

			// Refresca el archivo cada día
			if (archivoLocal.toFile().exists()) {

				// Interprete
				final Gson gson = new Gson();

				// Lee el archivo
				contenedorPersistencia = gson.fromJson(
					Files.newBufferedReader(archivoLocal),
					ContenedorPersistencia.class
				);

			}

		} catch (IOException ioe) {
			LOGGER.error("Error leyendo el archivo local de festivos " + archivoLocal, ioe);
		}

		// Entrega la persistencia
		return contenedorPersistencia;
	}

	/**
	 * Procedimiento que almacena la persistencia
	 * @param contenedorPersistencia registra la instancia del contenedor
	 */
	public static void setPersistencia(ContenedorPersistencia contenedorPersistencia) {
		// Archivo a persistir
		final Path archivoLocal = LOCAL ? PATH_PERSISTENCIA : Paths.get("/tmp", ARCHIVO_PERSISTENCIA);

		// Escribe el archivo
		final Gson gson = new Gson();
		final String json = gson.toJson(contenedorPersistencia);
		try {
			Files.write(archivoLocal, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException ioe) {
			LOGGER.error("Error escribiendo el archivo de persistencia local", ioe);
		}

		// AWS
		if(!LOCAL) {
			SubirArchivo.subirArchivo(archivoLocal);
		}
	}

}
