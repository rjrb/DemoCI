package com.ramirezblauvelt.democi.beans;

import java.time.LocalDate;

/**
 * Bean para recibir los festivos del servicio web "kayaposoft/enrico/json/v2.0/"
 */
public class Festivo {

	private String localName;
	private String englishName;
	private LocalDate date;

	@Override
	public String toString() {
		return "Festivo{" + "nombreLocal=" + localName + ", nombreIngles=" + englishName + ", fecha=" + date + '}';
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
