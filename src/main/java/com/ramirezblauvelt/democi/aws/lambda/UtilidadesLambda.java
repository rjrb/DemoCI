package com.ramirezblauvelt.democi.aws.lambda;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.ramirezblauvelt.democi.beans.PaisSoportado;
import com.ramirezblauvelt.democi.beans.PaisSoportadoAPI;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UtilidadesLambda {

	private UtilidadesLambda() {

	}

	/**
	 * Procedimiento que obtiene la lista de países soportados, a partir de la Lambda
	 *
	 * @return lista de países soportados
	 */
	public static ConcurrentMap<String, PaisSoportado> getPaisesSoportados() {
		// Instancia de AWS Lambda que entrega los países soportados
		final PaisesSoportadosLambda psService = LambdaInvokerFactory.builder()
             .lambdaClient(AWSLambdaClientBuilder.defaultClient())
             .build(PaisesSoportadosLambda.class)
		;

		// Invoca la Lambda
		final List<PaisSoportadoAPI> paises = psService.handleRequest();

		// Crea la estructura
		return crearListaPaisesSoportados(paises);
	}

	/**
	 * Genera la entidad a partir del objeto que entrega la API
	 *
	 * @param paises lista de países con la respuesta de la API
	 * @return lista de países soportados
	 */
	private static ConcurrentMap<String, PaisSoportado> crearListaPaisesSoportados(Collection<PaisSoportadoAPI> paises) {
		// Obtiene la estructura
		return paises.parallelStream()
	       .map(p -> {
		       final PaisSoportado ps = new PaisSoportado();
		       ps.setCountryCode(p.getCodigo());
		       ps.setFullName(p.getNombre());
		       return ps;
	       })
	       .collect(Collectors.toConcurrentMap(PaisSoportado::getCountryCode, Function.identity()))
		;
	}

}
