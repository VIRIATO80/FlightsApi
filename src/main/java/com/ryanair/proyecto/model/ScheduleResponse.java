package com.ryanair.proyecto.model;

import java.time.YearMonth;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduleResponse {

	private String origen;
	private String destino;
	private YearMonth yearMonth;

	@JsonProperty
	private Collection<Schedule> days;


	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Collection<Schedule> getDays() {
		return days;
	}

	public void setDays(Collection<Schedule> days) {
		this.days = days;
	}

	public YearMonth getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(YearMonth yearMonth) {
		this.yearMonth = yearMonth;
	}

}