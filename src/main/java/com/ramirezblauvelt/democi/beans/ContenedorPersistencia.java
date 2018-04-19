package com.ramirezblauvelt.democi.beans;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ContenedorPersistencia {

	private ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>> festivosGlobales = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, PaisSoportado> paisesSoportados = new ConcurrentHashMap<>();

	public ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>> getFestivosGlobales() {
		return festivosGlobales;
	}

	public void setFestivosGlobales(ConcurrentHashMap<String, ConcurrentHashMap<Integer, Set<LocalDate>>> festivosGlobales) {
		this.festivosGlobales = festivosGlobales;
	}

	public ConcurrentHashMap<String, PaisSoportado> getPaisesSoportados() {
		return paisesSoportados;
	}

	public void setPaisesSoportados(ConcurrentHashMap<String, PaisSoportado> paisesSoportados) {
		this.paisesSoportados = paisesSoportados;
	}

}
