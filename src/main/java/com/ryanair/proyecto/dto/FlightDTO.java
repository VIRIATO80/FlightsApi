package com.ryanair.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryanair.proyecto.model.Flight;


import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;


/**
 * Clase serializable para montar la respuesta JSON requerida envuelta correctamente.
 * 
 * @author Javier Lindo
 *
 */
public class FlightDTO implements Serializable {


	private static final long serialVersionUID = 1L;
	private int stops;
	private Collection<Flight> legs;


	@JsonCreator
	public FlightDTO(
			@JsonProperty("stops") int stops,
			@JsonProperty("legs") Collection<Flight> legs) {
		if(legs!=null){
			this.legs = legs.stream().collect(Collectors.toList());
			this.stops = stops;
		}
	}

	public Collection<Flight> getLegs() {
		return legs;
	}

	public int getStops() {
		return stops;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FlightDto{");
		sb.append("stops=").append(stops);
		sb.append(", legs=").append(legs);
		sb.append('}');
		return sb.toString();
	}
}