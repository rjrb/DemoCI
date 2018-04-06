package com.ramirezblauvelt.democi.beans;

import java.util.List;
import java.util.Objects;

public class PaisSoportado {

	public String countryCode;
	public List<String> regions;
	public List<String> holidayTypes;
	public String fullName;
	public FromDate fromDate;
	public ToDate toDate;

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

	public static class FromDate {
		public int day;
		public int month;
		public int year;
	}

	public static class ToDate {
		public int day;
		public int month;
		public int year;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
