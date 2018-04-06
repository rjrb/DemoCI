package com.ramirezblauvelt.democi.beans;

import java.util.List;
import java.util.Objects;

public class PaisSoportado {

	private String countryCode;
	private List<String> regions;
	private List<String> holidayTypes;
	private String fullName;
	private FromDate fromDate;
	private ToDate toDate;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PaisSoportado)) return false;
		PaisSoportado that = (PaisSoportado) o;
		return Objects.equals(countryCode, that.countryCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(countryCode);
	}

	@Override
	public String toString() {
		return "PaisSoportado{" +
				"countryCode='" + countryCode + '\'' +
				", fullName='" + fullName + '\'' +
				'}';
	}

	private static class FromDate {
		private int day;
		private int month;
		private int year;
	}

	private static class ToDate {
		private int day;
		private int month;
		private int year;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getFullName() {
		return fullName;
	}

}
