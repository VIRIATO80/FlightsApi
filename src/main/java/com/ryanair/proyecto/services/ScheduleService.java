package com.ryanair.proyecto.services;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Set;

import com.ryanair.proyecto.model.Flight;
import com.ryanair.proyecto.model.ScheduleResponse;

public interface ScheduleService {
	ScheduleResponse getSchedules(String origen, String destino,  YearMonth fecha);

	Set<Flight> getTrayectos(Set<ScheduleResponse> trayectosInicio, LocalDateTime departureDateTime,
			LocalDateTime arrivalTime);
}
