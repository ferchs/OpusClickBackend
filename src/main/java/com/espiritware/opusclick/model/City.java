package com.espiritware.opusclick.model;

public enum City {
	
	CALI ("Cali"), 
	BOGOTA ("Bogotá"), 
	MEDELLIN("Medellín");

	private String city;

	City(String city) {
        this.city = city;
    }

    public String city() {
        return city;
    }
}
