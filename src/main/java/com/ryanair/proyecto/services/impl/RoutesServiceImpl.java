package com.ryanair.proyecto.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryanair.proyecto.model.Route;
import com.ryanair.proyecto.rest.RoutesClient;
import com.ryanair.proyecto.services.RoutesService;



@Service("RoutesService")
public class RoutesServiceImpl implements RoutesService {

	@Autowired
	RoutesClient clienteRutas;

	@Override
	public Route[] dameRutas() {
		return clienteRutas.dameRutas();
	}


	/*
	 * Dado un origen, devuelve un listado de los códigos IATA hacia donde se puede volar
	 */
	public  List<String> getDestinosByOrigen(String origen, Route[] routes){


		Map<String, List<Route>> mapGrouped =
				Arrays.asList(routes).stream().collect(Collectors.groupingBy(w -> w.airportFrom));

		return mapGrouped.get(origen).stream()
				.map(Route::getAirportTo)
				.collect(Collectors.toList());

	}


	/*
	 * Dado un destino, devuelve un listado de los códigos IATA desde los que puede proceder un vuelo que aterriza en este aeropuerto
	 */
	public  List<String> getOrigenesByDestino(String destino, Route[] routes){


		Map<String, List<Route>> mapGrouped =
				Arrays.asList(routes).stream().collect(Collectors.groupingBy(w -> w.airportTo));

		return mapGrouped.get(destino).stream()
				.map(Route::getAirportFrom)
				.collect(Collectors.toList());		
	}


}
