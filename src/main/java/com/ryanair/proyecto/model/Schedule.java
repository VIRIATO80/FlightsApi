package com.ryanair.proyecto.model;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {


	@JsonProperty
	public Number day;

	@JsonProperty
	private Collection<Flight> flights;


	public Number getDay() {
		return day;
	}

	public void setDay(Number day) {
		this.day = day;
	}

	public Collection<Flight> getFlights() {
		return flights;
	}

	public void setFlights(Collection<Flight> flights) {
		this.flights = flights;
	}

}