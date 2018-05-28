package com.ramirezblauvelt.democi.aws.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.ramirezblauvelt.democi.aws.ConstantesAWS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class CrearTablaFestivos implements ConstantesAWS {

	/* Log de eventos */
	private static final Logger LOGGER = LogManager.getLogger();

	private CrearTablaFestivos() {

	}

	/**
	 * Crea la tabla de persistencia local de festivos
	 * @throws Exception error
	 */
	public static void crearTabla() throws Exception {
		// Obtiene el cliente
		final AmazonDynamoDB client = ConexionDynamoDB.getCliente();

		// Obtiene la conexión
		final DynamoDB dynamoDB = ConexionDynamoDB.getConexion(client);

		// Petición de creación de tabla
		final CreateTableRequest createTableRequest = new CreateTableRequest(
			Arrays.asList(
				new AttributeDefinition(LLAVE_PARTICION_FESTIVOS, ScalarAttributeType.S),
				new AttributeDefinition(LLAVE_ORDENAMIENTO_FESTIVOS, ScalarAttributeType.N)
			),
			TABLA_FESTIVOS,
			Arrays.asList(
				new KeySchemaElement(LLAVE_PARTICION_FESTIVOS, KeyType.HASH),
				new KeySchemaElement(LLAVE_ORDENAMIENTO_FESTIVOS, KeyType.RANGE)
			),
			new ProvisionedThroughput(10L, 10L)
		);

		// Crea la tabla si no existe
		LOGGER.info("Intentando crear la tabla '{}'", TABLA_FESTIVOS);
		final boolean tablaCreada = TableUtils.createTableIfNotExists(client, createTableRequest);
		LOGGER.info("Tabla creada (no existía): {}", tablaCreada);

		// Obtiene la instancia de la tabla
		final Table table = dynamoDB.getTable(TABLA_FESTIVOS);

		// Espera a que esté activa
		table.waitForActive();
	}

}
