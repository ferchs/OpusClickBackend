package com.espiritware.opusclick.model;

public enum Availability {
	
	AVAILABLE ("Disponible"), 
	PARTIALLY_AVAILABLE ("Parcialmente disponible"), 
	OCCUPIED("Ocupado");
	
	private String availability;

	Availability(String availability) {
        this.availability = availability;
    }

    public String availability() {
        return availability;
    }

}
