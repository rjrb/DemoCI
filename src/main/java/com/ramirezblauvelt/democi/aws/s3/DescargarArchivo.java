package com.ramirezblauvelt.democi.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ramirezblauvelt.democi.aws.ConstantesAWS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DescargarArchivo implements ConstantesAWS {

	private DescargarArchivo() {

	}

	/**
	 * Procedimiento que descarga un archivo desde S3
	 * @param archivo instancia del archivo local a crear
	 */
	public static void descargarArchivo(Path archivo) {
		// Añade el archivo a S3
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
		try {
			// Referencia al objeto persistido
			final S3Object o = s3.getObject(BUCKET, ARCHIVO_PERSISTENCIA);

			// Flujo de entrada
			try (S3ObjectInputStream s3is = o.getObjectContent()) {
				// Flujo de salida
				try (OutputStream os = Files.newOutputStream(archivo)) {
					// Escribe el archivo
					byte[] read_buf = new byte[1024];
					int read_len;
					while ((read_len = s3is.read(read_buf)) > 0) {
						os.write(read_buf, 0, read_len);
					}
				}
			}

		} catch (AmazonServiceException e) {
			System.err.println("Excepción en el servicio de AWS: " + e.getErrorMessage());
		} catch (FileNotFoundException e) {
			System.err.println("Archivo no encontrado: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Excepción IO general: " + e.getMessage());
		}

	}
}
