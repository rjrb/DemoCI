package com.ramirezblauvelt.democi.aws.dynamo;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.ramirezblauvelt.democi.aws.ConstantesAWS;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Set;

public class AdicionarFestivos implements ConstantesAWS {

	/* Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();

	private AdicionarFestivos() {

	}

	/**
	 * Procedimiento que adiciona festivos a la persistencia
	 * @param contenedorPersistencia el contenedor local en memoria
	 */
	public static void adicionarFestivos(ContenedorPersistencia contenedorPersistencia) {
		// Asegura la existencia de la tabla
		try {
			CrearTablaFestivos.crearTabla();
		} catch (Exception e) {
			LOGGER.error("Error creando la tabla de festivos", e);
		}

		// Obtiene la conexión
		final DynamoDB dynamoDB = ConexionDynamoDB.getConexion();

		// Obtiene la tabla
		final Table table = dynamoDB.getTable(TABLA_FESTIVOS);

		try {
			// Recorre los países
			for (String pais : contenedorPersistencia.getFestivosGlobales().keySet()) {
				// Recorre los años
				for (Integer year : contenedorPersistencia.getFestivosGlobales().get(pais).keySet()) {
					// Festivos del year
					final Set<LocalDate> festivosYYYY = contenedorPersistencia.getFestivosGlobales().get(pais).get(year);

					// Inserta los festivos
					final PutItemOutcome outcome = table.putItem(
						new Item()
							.withPrimaryKey(LLAVE_PARTICION_FESTIVOS, pais)
							.withList(LLAVE_ORDENAMIENTO_FESTIVOS, festivosYYYY)
					);

					// Resultado
					LOGGER.info("Registros adicionados con éxito", outcome.getPutItemResult());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error añadiendo registros a DynamoDB", e);
		}
	}
}
