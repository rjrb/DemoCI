package com.ramirezblauvelt.democi.aws.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class UtilidadesDynamoDB {

	/* Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Consulta todos los festivos registrados en el sistema
	 * @return la estructura de datos en memoria
	 */
	public static ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> getFestivos() {
		// Crea el objeto
		final ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> festivosGlobales = new ConcurrentHashMap<>();

		// Consulta
		final List<EntidadFestivos> items = new DynamoDBMapper(ConexionDynamoDB.getCliente()).scan(EntidadFestivos.class, new DynamoDBScanExpression());

		// Recorre los resultados cargando el mapa
		for(EntidadFestivos f : items) {
			// País
			festivosGlobales.putIfAbsent(f.getPais(), new ConcurrentHashMap<>());

			// Convierte los festivos
			final Set<LocalDate> festivos = f.getFestivos().parallelStream().map(LocalDate::parse).collect(Collectors.toSet());

			// Año
			festivosGlobales.get(f.getPais()).putIfAbsent(f.getYyyy(), festivos);
		}

		// Entrega los festivos
		return festivosGlobales;
	}

	/**
	 * Procedimiento que adiciona festivos a la persistencia
	 * @param contenedorPersistencia el contenedor local en memoria
	 */
	public static void setFestivos(ContenedorPersistencia contenedorPersistencia) {
		// Cliente
		final AmazonDynamoDB amazonDynamoDB = ConexionDynamoDB.getCliente();

		// Obtiene el mapper para la conexión
		final DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

		// Asegura la existencia de la tabla
		final CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(EntidadFestivos.class);
		createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		final boolean notExists = TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
		if(notExists) {
			LOGGER.info("Tabla creada con éxito");
		}

		// Crea los ítems
		try {
			// Recorre los países
			for (String pais : contenedorPersistencia.getFestivosGlobales().keySet()) {
				// Recorre los años
				for (Integer year : contenedorPersistencia.getFestivosGlobales().get(pais).keySet()) {
					// Festivos del año
					final List<String> festivosYYYY = contenedorPersistencia.getFestivosGlobales().get(pais).get(year)
	                      .parallelStream()
	                      .map(LocalDate::toString)
	                      .collect(Collectors.toList())
					;

					// Ítem
					final EntidadFestivos item = new EntidadFestivos();
					item.setPais(pais);
					item.setYyyy(year);
					item.setFestivos(festivosYYYY);

					// Guarda el registro
					mapper.save(item);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error añadiendo registros a DynamoDB", e);
		}
	}

}
