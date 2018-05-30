package com.ramirezblauvelt.democi.aws.dynamo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class ConexionDynamoDB {

	private ConexionDynamoDB() {

	}

	/**
	 * Entrega el cliente de conexión a la base de datos
	 * @return cliente de DynamoDB
	 */
	public static AmazonDynamoDB getCliente() {
		// Crea el cliente
		return AmazonDynamoDBClientBuilder
	       .standard()
		       .withRegion(Regions.US_EAST_1)
		       .build()
		;
	}

	/**
	 * Entrega una conexión a la base de datos
	 * @param cliente el cliente de conexión a la base de datos
	 * @return una instancia de la base de datos
	 */
	public static DynamoDB getConexion(AmazonDynamoDB cliente) {
		return new DynamoDB(cliente);
	}

	/**
	 * Entrega una conexión a la base de datos
	 * @return una instancia de la base de datos
	 */
	public static DynamoDB getConexion() {
		return new DynamoDB(getCliente());
	}

}
