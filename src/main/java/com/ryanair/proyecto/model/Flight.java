package com.ryanair.proyecto.model;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Flight {

	private String origen;	
	private String destino;	
	private String number;
	private String departureTime;  
	private String arrivalTime;

	@JsonIgnore
	private LocalDateTime fSalida;
	
	@JsonIgnore
	private LocalDateTime fLlegada;


	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}

	public String getDestino() {
		return destino;
	}
	
	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	public String getOrigen() {
		return origen;
	}
	
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	public String getDepartureTime() {
		return departureTime;
	}
	
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	
	public String getArrivalTime() {
		return arrivalTime;
	}
	
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public LocalDateTime getfSalida() {
		return fSalida;
	}
	
	public void setfSalida(LocalDateTime fSalida) {
		this.fSalida = fSalida;
	}
	
	public LocalDateTime getfLlegada() {
		return fLlegada;
	}
	
	public void setfLlegada(LocalDateTime fLlegada) {
		this.fLlegada = fLlegada;
	}
}
