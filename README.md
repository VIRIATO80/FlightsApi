# Flights API REST

● The application responses to following request URI with given query parameters:

```
http://<HOST>/<CONTEXT>/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}arrivalDateTime={arrivalDateTime}
``` 
where:

* ❍ departure - a departure airport IATA code 
* ❍ departureDateTime - a departure datetime in the departure airport timezone in ISO format  
* ❍ arrival - an arrival airport IATA code 
* ❍ arrivalDateTime - an arrival datetime in the arrival airport timezone in ISO format 

For example:
```
http://localhost:8080/somevalidcontext/interconnections?departure=DUB&arrival=WRO&departureDateTime=2016-03-01T07:00&arrivalDateTime=2016-03-03T21:00
```

● The application returns a list of flights departing from a given departure airport not earlier than the specified departure datetime and arriving to a given arrival airport not later than the specified arrival datetime. 
The list consists of:
* ❍ all direct flights if available (for example: DUB - WRO) *
* ❍ all interconnected flights with a maximum of one stop if available (for example: DUB - STN -WRO) 

● For interconnected flights the difference between the arrival and the next departure is 2h or greater.
