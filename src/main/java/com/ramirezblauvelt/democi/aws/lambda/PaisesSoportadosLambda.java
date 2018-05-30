package com.ramirezblauvelt.democi.aws.lambda;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.ramirezblauvelt.democi.beans.PaisSoportadoAPI;

import java.util.List;

public interface PaisesSoportadosLambda {

	@LambdaFunction(functionName="PaisesSoportados")
	List<PaisSoportadoAPI> handle_request();

}
