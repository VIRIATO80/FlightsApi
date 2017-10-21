package com.ryanair.proyecto.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ryanair.proyecto.model.Route;

/**
 * Clase cliente que conecta con el endpoint de RyanAir para obtener posibles pares de origen - destino
 * @author Javier Lindo
 *
 */

@Component
public class RoutesClient {

	/**
	 * Conectándose al API de RyanAir podemos obtener objetos [origen-destino] en formato IATA
	 * @return Devuelve una lista de posibles rutas de orígenes y destinos IATA
	 */
	public Route[] dameRutas()
	{
		String uri = "https://api.ryanair.com/core/3/routes/";

		RestTemplate template = new RestTemplate();

		ResponseEntity<Route[]> routesResponse = template.getForEntity(uri, Route[].class);		
		if(routesResponse.getStatusCode()== HttpStatus.OK){
			Route[] routes = routesResponse.getBody();
			return routes;
		}
		return null;					
	}
}
