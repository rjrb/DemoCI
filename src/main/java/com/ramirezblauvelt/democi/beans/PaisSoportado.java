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

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}
	}

	private static class ToDate {
		private int day;
		private int month;
		private int year;

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public List<String> getRegions() {
		return regions;
	}

	public void setRegions(List<String> regions) {
		this.regions = regions;
	}

	public List<String> getHolidayTypes() {
		return holidayTypes;
	}

	public void setHolidayTypes(List<String> holidayTypes) {
		this.holidayTypes = holidayTypes;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public FromDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(FromDate fromDate) {
		this.fromDate = fromDate;
	}

	public ToDate getToDate() {
		return toDate;
	}

	public void setToDate(ToDate toDate) {
		this.toDate = toDate;
	}

}
