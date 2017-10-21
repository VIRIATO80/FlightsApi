package com.ryanair.proyecto.utils;


import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase de métodos auxiliares
 */
public class Utils {


	/**
	 * Crea una fecha a partir de los parámetros recibidos:
	 * @param anioMes - Objecto que representa un añoMes
	 * @param dia - Día del mes
	 * @param hora - String de la hora del día. El formato recibido será HH:MM
	 * @return Un objeto LocalDateTime
	 */
	public static LocalDateTime crearFecha( YearMonth anioMes, int dia, String hora){

		return LocalDateTime.of(anioMes.getYear(), anioMes.getMonthValue() , dia, Integer.parseInt(hora.substring(0,2)), Integer.parseInt(hora.substring(3,5)) );
	}

	/**
	 * Elimina los minutos de un LocalDateTime en formato ISO
	 * @param dateTime - Fecha en formato ISO
	 * @return Fecha sin minutos
	 */
	public static LocalDateTime truncar(LocalDateTime dateTime) {
		return dateTime.truncatedTo(ChronoUnit.MINUTES);
	}


	/**
	 * Método que recibidas dos fechas nos devuelve una lista de objetos YearMonth que existen entre ellas
	 * @param start Fecha de inicio
	 * @param end Fecha de Fin
	 * @return Lista de meses contenidos en el rango
	 */
	public static List<YearMonth> dameMeses(LocalDateTime start, LocalDateTime end) {
		
		if(start.isAfter(end)) {
			List<YearMonth> months = dameMeses(end, start);
			Collections.reverse(months);
			return months;
		} else {
			YearMonth from = YearMonth.from(start);
			YearMonth to = YearMonth.from(end);
			List<YearMonth> months = new LinkedList<>();
			months.add(from);
			if (to.isAfter(from)) {
				YearMonth m = from;
				do {
					m = m.plusMonths(1);
					months.add(m);
				}
				while (m.isBefore(to));
			}
			return months;
		}
	}


}
