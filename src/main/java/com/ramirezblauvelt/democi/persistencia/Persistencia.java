package com.ramirezblauvelt.democi.persistencia;

import com.ramirezblauvelt.democi.aws.ConstantesAWS;
import com.ramirezblauvelt.democi.beans.ContenedorPersistencia;

public interface Persistencia extends ConstantesAWS {

	/**
	 * Establece la persistencia local accesible, según el lugar de ejecución
	 * @param contenedorPersistencia la instancia del contenedor a persistir
	 */
	void setPersistencia(ContenedorPersistencia contenedorPersistencia);

	/**
	 * Genera el contenedor de datos local en memoria para la ejecución de los procesos
	 * @return el contenedor en memoria generado desde la persistencia
	 */
	ContenedorPersistencia getPersistencia();

}
