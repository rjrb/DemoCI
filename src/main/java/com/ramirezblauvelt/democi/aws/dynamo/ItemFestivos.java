package com.ramirezblauvelt.democi.aws.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "Festivos")
public class ItemFestivos {

	private String pais;
	private int yyyy;
	private List<String> festivos;

	@DynamoDBHashKey(attributeName = "pais")
	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@DynamoDBRangeKey(attributeName = "yyyy")
	public int getYyyy() {
		return yyyy;
	}

	public void setYyyy(int yyyy) {
		this.yyyy = yyyy;
	}

	@DynamoDBAttribute(attributeName = "festivos")
	public List<String> getFestivos() {
		return festivos;
	}

	public void setFestivos(List<String> festivos) {
		this.festivos = festivos;
	}

}
