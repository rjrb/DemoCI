package com.ramirezblauvelt.democi.aws.dynamo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

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

}
