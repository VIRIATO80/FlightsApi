package com.ryanair.proyecto.services;

import java.util.List;

import com.ryanair.proyecto.model.Route;


public interface RoutesService {
	public Route[] dameRutas();

	public List<String> getDestinosByOrigen(String departure, Route[] rutas);

	public List<String> getOrigenesByDestino(String arrival, Route[] rutas);


}
