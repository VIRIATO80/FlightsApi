package com.ryanair.proyecto.services.impl;


import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.ryanair.proyecto.dto.FlightDTO;
import com.ryanair.proyecto.model.Flight;
import com.ryanair.proyecto.model.Route;
import com.ryanair.proyecto.model.ScheduleResponse;
import com.ryanair.proyecto.services.FlightService;
import com.ryanair.proyecto.services.RoutesService;
import com.ryanair.proyecto.services.ScheduleService;
import com.ryanair.proyecto.utils.Utils;

/**
 * Servicio que apoyándose en los clientes de Rutas y de Planing mensual devuelve una respuesta válida de vuelos directos 
 * y vuelos con una ciudad de conexión
 * @author Javier Lindo
 *
 */
@Service("FlightService")
public class FlightServiceImpl implements FlightService{


	@Autowired
	RoutesService rutasService;

	@Autowired
	ScheduleService scheduleService;

	/**
	 * 
	 * @return Pares de rutas origen - destino
	 */
	public Route[] dameRutas(){
		return rutasService.dameRutas();
	}


	/**
	 * Método que obtiene los vuelos directos y con una escala entre dos ciudades dadas y en un rango de fechas marcado
	 */
	public List<FlightDTO> getInterconnections(String departure, String arrival, 
			LocalDateTime departureDateTime, LocalDateTime arrivalDateTime ) {


		Route[] rutas = dameRutas();

		List<String> ciudadesConexion = getConexiones(departure, arrival, rutas);           
		List<YearMonth> months = Utils.dameMeses(departureDateTime, arrivalDateTime);               
		Set<ScheduleResponse> trayectosDirectos = new HashSet<ScheduleResponse>();
		Set<ScheduleResponse> trayectoInicio = new HashSet<ScheduleResponse>();
		Set<ScheduleResponse> trayectoFinal = new HashSet<ScheduleResponse>();

		for(YearMonth month : months){

			//Trayectos directos 
			trayectosDirectos.add(getSchedules(departure, arrival,  month));

			//Trayectos con una sola ciudad de conexión
			for (String codIataConexion : ciudadesConexion) {
				try{
					trayectoInicio.add(getSchedules(departure, codIataConexion,  month));
					trayectoFinal.add(getSchedules(codIataConexion, arrival, month));
				}catch(HttpClientErrorException ex){}
			}
		}

		List<FlightDTO> directFlights = getVuelosDirectos(trayectosDirectos, departureDateTime, arrivalDateTime);                
		List<FlightDTO> connectionflights = getConexionesValidas(trayectoInicio, trayectoFinal, departureDateTime, arrivalDateTime);

		directFlights.addAll(connectionflights);

		return directFlights;           
	} 
	
	
	/**
	 * 
	 * @param departure Código IATA ciudad salida
	 * @param arrival Código IATA ciudad llegada
	 * @param rutas Posibles rutas comerciales 
	 * @return Devuelve un listado de códigos IATA de ciudades que pueden servir para conectar un aeropuerto de salida y otro de llegada
	 */
	private List<String> getConexiones(String departure, String arrival, Route[] rutas){

		List<String> destinosPosibles = rutasService.getDestinosByOrigen(departure, rutas);		
		List<String> origenesPosibles = rutasService.getOrigenesByDestino(arrival, rutas);
		//Nos quedamos con los códigos IATA de las ciudades que pueden servir de posible conexión
		destinosPosibles.retainAll(origenesPosibles);

		return destinosPosibles;	
	}


	/**
	 * Obtiene el planning mensual de vuelos que conectan dos ciudades cualquiera
	 * @param origen Código IATA de ciudad de salida
	 * @param destino Código IATA de ciudad de destino
	 * @param fecha Mes año del vuelo
	 * @return
	 */
	private ScheduleResponse getSchedules(String origen, String destino,  YearMonth fecha ) {

		return scheduleService.getSchedules(origen, destino,  fecha);
	}



	/**
	 * Obtiene los vuelos directos entre dos ciudades para un rango de fechas
	 * @param trayectos Planning mensual que une dos ciudades
	 * @param departureDateTime Fecha desde
	 * @param arrivalTime Fecha hasta
	 * @return Devuelve un listado de vuelos. Este listado puede ser de vuelos directos o vuelos con una sola escala de conexión
	 */
	private List<FlightDTO> getVuelosDirectos(Set<ScheduleResponse> trayectos, LocalDateTime departureDateTime, LocalDateTime arrivalTime ) {

		Set<Flight> vuelosDirectos =  scheduleService.getTrayectos(trayectos, departureDateTime, arrivalTime);		
		List<FlightDTO> response = new ArrayList<FlightDTO>();

		for(Flight vuelo : vuelosDirectos){
			ArrayList<Flight> arr = new ArrayList<Flight>();
			arr.add(vuelo);
			FlightDTO dto = new FlightDTO(0, arr);
			response.add(dto);
		}

		return response;			
	}


	/**
	 * Tras obtener los vuelos válidos de un trayecto de ida basado en una conexión y de un trayecto de vuelta,
	 * este método devuelve una combinación de los vuelos válidos que hacen escala.
	 * @param trayectosInicio Trayectos desde la ciudad de origen deseada a un punto válido de conexión
	 * @param trayectosFinal Trayectos desde una ciudad de conexión al destino deseado.
	 * @param departureDateTime Fecha desde la que pueden salir los vuelos desde la ciudad de origen
	 * @param arrivalTime Fecha tope para la llegada del último vuelo a destino
	 * @return Trayectos con una escala que conectan las ciudades que hemos introducio en el request
	 */
	private List<FlightDTO> getConexionesValidas(Set<ScheduleResponse> trayectosInicio,
			Set<ScheduleResponse> trayectosFinal, LocalDateTime departureDateTime, LocalDateTime arrivalTime ) {


		Set<Flight> vuelosTrayectoIda =  scheduleService.getTrayectos(trayectosInicio, departureDateTime, arrivalTime);	
		Set<Flight> vuelosTrayectoVuelta =  scheduleService.getTrayectos(trayectosFinal, departureDateTime, arrivalTime);	
		List<ArrayList<Flight>> combinacion = combine(vuelosTrayectoIda, vuelosTrayectoVuelta);

		List<FlightDTO> response = new ArrayList<FlightDTO>();
		for(ArrayList<Flight> listaVuelos : combinacion){
			FlightDTO dto = new FlightDTO(1, listaVuelos);
			response.add(dto);
		}

		return response;		
	}


	/**
	 * 
	 * @param one Conjunto de vuelos desde origen a ciudad de escala
	 * @param two Conjunto de vuelos desde ciudad de escala a destino
	 * @return Devuelve los vuelos que cumplen con los requisitos del API:
	 * 	Deben estar entre dos fechas 
	 *  La conexión en la ciudad puente tiene que darse como mínimo con dos horas de margen
	 *  El destino del primer vuelo debe ser el origen del segundo para garantizar la escala
	 */
	private List<ArrayList<Flight>> combine(Set<Flight> one, Set<Flight> two)
	{
		List<ArrayList<Flight>> combs=new ArrayList<ArrayList<Flight>>();
		LocalDateTime fechaInicio;
		LocalDateTime fechaFin;

		for(Flight e:one)
		{
			for(Flight e2:two)
			{
				ArrayList<Flight> ps=new ArrayList<Flight>();

				fechaInicio = e.getfLlegada();
				fechaFin = e2.getfSalida();

				if(fechaInicio.plusHours(2).isBefore(fechaFin) && e.getDestino().equals(e2.getOrigen())){	            	
					ps.add(e);
					ps.add(e2);
					combs.add(ps);	            	
				}
			}
		}
		return combs;
	}


}
