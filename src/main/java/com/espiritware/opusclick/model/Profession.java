package com.espiritware.opusclick.model;

public enum Profession {
	
	CARPENTRY ("Carpintería"), 
	PAINTING ("Pintura"), 
	PLUMBING("Plomería");
	
	private String profession;

	Profession(String profession) {
        this.profession = profession;
    }

    public String profession() {
        return profession;
    }
    
}
