package com.espiritware.opusclick.model;

public enum Type {
	
	POSITIVE ("POSITIVE"),
	NEUTRAL ("NEUTRAL"), 
	NEGATIVE ("NEGATIVE");
	
	private String type;

	Type(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
