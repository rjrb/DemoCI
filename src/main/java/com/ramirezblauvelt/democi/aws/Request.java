package com.ramirezblauvelt.democi.aws;

import java.time.LocalDate;

public class Request {

	private LocalDate fechaInicial;
	private int diasHabilesSumar;
	private String pais;

	public Request() {
	}

	public Request(LocalDate fechaInicial, int diasHabilesSumar, String pais) {
		this.fechaInicial = fechaInicial;
		this.diasHabilesSumar = diasHabilesSumar;
		this.pais = pais;
	}

	public LocalDate getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(LocalDate fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public int getDiasHabilesSumar() {
		return diasHabilesSumar;
	}

	public void setDiasHabilesSumar(int diasHabilesSumar) {
		this.diasHabilesSumar = diasHabilesSumar;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

}
