package com.ramirezblauvelt.democi.beans;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ContenedorPersistencia {

	private ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> festivosGlobales = new ConcurrentHashMap<>();
	private ConcurrentMap<String, PaisSoportado> paisesSoportados = new ConcurrentHashMap<>();

	public ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> getFestivosGlobales() {
		return festivosGlobales;
	}

	public void setFestivosGlobales(ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> festivosGlobales) {
		this.festivosGlobales = festivosGlobales;
	}

	public ConcurrentMap<String, PaisSoportado> getPaisesSoportados() {
		return paisesSoportados;
	}

	public void setPaisesSoportados(ConcurrentMap<String, PaisSoportado> paisesSoportados) {
		this.paisesSoportados = paisesSoportados;
	}

}
