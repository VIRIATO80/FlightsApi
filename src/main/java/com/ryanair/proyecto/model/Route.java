package com.ryanair.proyecto.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

	public String airportFrom;
	public String airportTo;


	public String getAirportFrom() {
		return airportFrom;
	}

	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}

	public String getAirportTo() {
		return airportTo;
	}

	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}


}
