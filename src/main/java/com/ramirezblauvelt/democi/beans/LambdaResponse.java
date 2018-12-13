package com.ramirezblauvelt.democi.beans;

import java.util.Objects;

public class LambdaResponse {

	private String resultado;

	public LambdaResponse() {

	}

	public LambdaResponse(String resultado) {
		this.resultado = resultado;
	}

	@Override
	public String toString() {
		return "LambdaResponse{" +
			"resultado='" + resultado + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LambdaResponse)) return false;
		LambdaResponse that = (LambdaResponse) o;
		return Objects.equals(resultado, that.resultado);
	}

	@Override
	public int hashCode() {
		return Objects.hash(resultado);
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

}
