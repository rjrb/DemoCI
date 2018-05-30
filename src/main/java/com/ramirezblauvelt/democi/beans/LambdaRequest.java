package com.ramirezblauvelt.democi.beans;

public class LambdaRequest {

	private String fechaInicial;
	private int diasHabilesSumar;
	private String pais;

	public LambdaRequest() {
	}

	public LambdaRequest(String fechaInicial, int diasHabilesSumar, String pais) {
		this.fechaInicial = fechaInicial;
		this.diasHabilesSumar = diasHabilesSumar;
		this.pais = pais;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
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
