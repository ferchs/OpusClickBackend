package com.espiritware.opusclick.model;

public enum Type {
	
	POSITIVE ("Positive"), 
	NEGATIVE ("Negative");
	
	private String type;

	Type(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
