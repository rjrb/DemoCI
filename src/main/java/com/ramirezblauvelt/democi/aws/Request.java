package com.ramirezblauvelt.democi.aws;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Request {

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate fechaInicial;
	private int diasHabilesSumar;
	private String pais;

	public Request() {
	}

	public Request(LocalDate fechaInicial, int diasHabilesSumar, String pais) {
		this.fechaInicial = fechaInicial;
		this.diasHabilesSumar = diasHabilesSumar;
		this.pais = pais;
	}

	public LocalDate getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(LocalDate fechaInicial) {
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

	/**
	 * Permite de-serializar la clase LocalDate desde Jackson (para AWS)
	 */
	public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

		private static final long serialVersionUID = 1L;

		protected LocalDateDeserializer() {
			super(LocalDate.class);
		}

		@Override
		public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException {
			return LocalDate.parse(jp.readValueAs(String.class));
		}

	}

	public class LocalDateSerializer extends StdSerializer<LocalDate> {

		private static final long serialVersionUID = 1L;

		public LocalDateSerializer() {
			super(LocalDate.class);
		}

		@Override
		public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider sp) throws IOException {
			gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
		}
	}

}
