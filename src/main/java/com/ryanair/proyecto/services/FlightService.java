package com.ryanair.proyecto.services;

import java.time.LocalDateTime;
import java.util.List;

import com.ryanair.proyecto.dto.FlightDTO;


public interface FlightService {

	public List<FlightDTO> getInterconnections(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime );
}
