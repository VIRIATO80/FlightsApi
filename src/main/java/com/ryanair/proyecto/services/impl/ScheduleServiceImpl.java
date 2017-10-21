package com.ryanair.proyecto.services.impl;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.proyecto.model.Flight;
import com.ryanair.proyecto.model.Schedule;
import com.ryanair.proyecto.model.ScheduleResponse;
import com.ryanair.proyecto.rest.SchedulesClient;
import com.ryanair.proyecto.services.ScheduleService;
import com.ryanair.proyecto.utils.Utils;


@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	SchedulesClient scheduleClient;

	/**
	 * Método que conectándose al API de RyanAir nos devuelve el TimeTable mensual que conecta dos ciudades cualquiera
	 * @param  origen Ciudad origen
	 * @param destino Ciudad destino
	 * @param fecha Mes y año de los vuelos a devolver
	 * @return Devuelve un Timetable de un mes para un origen destino especificado
	 */
	public ScheduleResponse getSchedules(String origen, String destino,  YearMonth fecha ) {
		return scheduleClient.getSchedules(origen, destino, fecha);		
	}

	/**
	 * Devuelve trayectos entre dos fechas dadas y rellena en el objeto vuelo datos como una fecha formateada, el origen y el destino IATA
	 */
	public Set<Flight> getTrayectos(Set<ScheduleResponse> trayectosMes, LocalDateTime fechaInicio, LocalDateTime fechafin){

		Set<Flight> vuelosValidos = new HashSet<Flight>();		

		for (ScheduleResponse trayectosDia : trayectosMes) {				

			Collection<Schedule>  mapaVuelos = trayectosDia.getDays();

			for (Schedule planningDay : mapaVuelos) {
				if(planningDay.getDay().intValue() >= fechaInicio.getDayOfMonth() && planningDay.getDay().intValue() <= fechafin.getDayOfMonth()){
					for (Flight vuelo : planningDay.getFlights()) {	
						
						//Rellenamos origen y destino
						vuelo.setOrigen(trayectosDia.getOrigen());
						vuelo.setDestino(trayectosDia.getDestino());

						//Rellenamos la fecha de salida
						LocalDateTime fechaSalida = Utils.crearFecha( trayectosDia.getYearMonth() , planningDay.getDay().intValue(),  vuelo.getDepartureTime());								
						vuelo.setfSalida(fechaSalida);
						vuelo.setDepartureTime(vuelo.getfSalida().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

						//Rellenamos la hora de llegada						
						LocalDateTime fechaLlegada = Utils.crearFecha( trayectosDia.getYearMonth() , planningDay.getDay().intValue(),  vuelo.getArrivalTime());	
						vuelo.setfLlegada(fechaLlegada);
						vuelo.setArrivalTime(vuelo.getfLlegada().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

						//Comprobamos que la fecha del vuelo está entre las marcadas antes de añadir al listado de trayectos
						if(vuelo.getfSalida().isAfter(fechaInicio) && vuelo.getfLlegada().isBefore(fechafin)){								
							vuelosValidos.add(vuelo);
						}
					}
				}
			}			

		}
		return vuelosValidos; 
	}

}
