package com.ramirezblauvelt.democi.persistencia;

import com.ramirezblauvelt.democi.aws.dynamo.UtilidadesDynamoDB;
import com.ramirezblauvelt.democi.aws.lambda.UtilidadesLambda;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;
import com.ramirezblauvelt.democi.beans.PaisSoportado;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class PersistenciaAWS implements Persistencia {

	@Override
	public void setPersistencia(ContenedorPersistencia contenedorPersistencia) {
		// Sólo registra los festivos, pues los países los entrega otra API
		UtilidadesDynamoDB.setFestivos(contenedorPersistencia);
	}

	@Override
	public ContenedorPersistencia getPersistencia() {
		// Obtiene los festivos
		final ConcurrentMap<String, ConcurrentMap<Integer, Set<LocalDate>>> festivosGlobales = UtilidadesDynamoDB.getFestivos();

		// Obtiene los países soportados
		final ConcurrentMap<String, PaisSoportado> paisesSoportados = UtilidadesLambda.getPaisesSoportados();

		// Crea el contenedor
		final ContenedorPersistencia contenedorPersistencia = new ContenedorPersistencia();
		contenedorPersistencia.setFestivosGlobales(festivosGlobales);
		contenedorPersistencia.setPaisesSoportados(paisesSoportados);

		// Entrega el contenedor
		return contenedorPersistencia;
	}

}