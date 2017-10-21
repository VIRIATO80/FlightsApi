package com.ryanair.proyecto;


import java.util.ArrayList;
import java.util.List;


import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.ryanair.proyecto.dto.FlightDTO;
import com.ryanair.proyecto.services.FlightService;
import com.ryanair.proyecto.utils.Utils;

/**
 * Controlador Rest que recibe una petición y devuelve una combinación de vuelos en JSON
 * The application should return a list of flights departing from a given departure airport not earlier 
 * than the specified departure datetime and arriving to a given arrival airport not later than the 
 * specified arrival datetime. The list should consist of:
 *  - All direct flights if available (for example: DUB - WRO)
 *  - All interconnected flights with a maximum of one stop if available (for example: DUB - STN - WRO)
 * 
 * @author Javier Lindo
 */


@RestController
public class FlightsController {

	@Autowired
	private FlightService flightService;


	public FlightsController(FlightService flightService) {
		this.flightService = flightService;
	}

	/**
	 * The method should response to following request URI with given query parameters: 
	 * http://<HOST>/<CONTEXT>/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime} where:
	 * @param departure - a departure airport IATA code
	 * @param departureDateTime - a departure datetime in the departure airport timezone in ISO format
	 * @param arrival - an arrival airport IATA code
	 * @param arrivalDateTime - an arrival datetime in the arrival airport timezone in ISO format
	 * @return JSON response
	 */

	@RequestMapping(value = "/interconnections" , method = RequestMethod.GET)
	public ResponseEntity<List<FlightDTO>> getInterconnections(String departure, String arrival, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("departureDateTime") LocalDateTime departureDateTime, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("arrivalDateTime") LocalDateTime arrivalDateTime ) {

		LocalDateTime inicio = Utils.truncar(departureDateTime);
		LocalDateTime fin = Utils.truncar(arrivalDateTime);

		HttpHeaders cabeceras = new HttpHeaders();

		if(departure.equals(arrival)) 
			return new ResponseEntity<>(new ArrayList<FlightDTO>(), cabeceras, HttpStatus.OK);    
		if(departureDateTime.compareTo(arrivalDateTime) >= 0)
			return new ResponseEntity<>(new ArrayList<FlightDTO>(), cabeceras, HttpStatus.OK); 


		List<FlightDTO> vuelosResultado = flightService.getInterconnections(departure, arrival, inicio, fin); 
		return new ResponseEntity<>(vuelosResultado, cabeceras, HttpStatus.OK); 

	} 

}
