package com.ryanair.proyecto.rest;

import java.time.YearMonth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ryanair.proyecto.model.ScheduleResponse;

/**
 * Clase cliente que conecta con el endpoint de RyanAir para obtener la planificación por mes de las rutas especificadas.
 * @author Javier Lindo
 *
 */

@Component
public class SchedulesClient {

	/**
	 * 
	 * @param origen Código IATA de la ciudad origen
	 * @param destino Código IATA de la ciudad destino
	 * @param fecha Fecha válida (Mes y año)
	 * @return Devuelve un wrapper para una Timetable mensual
	 */
	public ScheduleResponse getSchedules(String origen, String destino,  YearMonth fecha ) {
			
		String uri = "https://api.ryanair.com/timetable/3/schedules/"+origen+"/"+destino+"/years/"+fecha.getYear()+"/months/"+fecha.getMonthValue();
		
		RestTemplate template = new RestTemplate();
		
		ResponseEntity<ScheduleResponse>  response = template.getForEntity(uri, ScheduleResponse.class);
		if(response != null && response.getStatusCode()== HttpStatus.OK){
			ScheduleResponse respuesta = response.getBody();
			respuesta.setOrigen(origen);
			respuesta.setDestino(destino);
			respuesta.setYearMonth(fecha);
			return respuesta;
		}else{
			return null;
		}			
	}
	
}
