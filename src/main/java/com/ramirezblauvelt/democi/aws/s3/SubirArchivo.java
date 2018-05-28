package com.ramirezblauvelt.democi.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ramirezblauvelt.democi.aws.ConstantesAWS;

import java.nio.file.Path;

public class SubirArchivo implements ConstantesAWS {

	private SubirArchivo() {

	}

	/**
	 * Procedimiento que sube el archivo a S3
	 * @param archivo archivo local a subir
	 */
	public static void subirArchivo(Path archivo) {
		// Añade el archivo a S3
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
		try {
			s3.putObject(BUCKET, ARCHIVO_PERSISTENCIA, archivo.toFile());
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
	}
}
