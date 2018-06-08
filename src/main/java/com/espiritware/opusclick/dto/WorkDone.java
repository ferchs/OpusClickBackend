package com.espiritware.opusclick.dto;

import lombok.Getter;

@Getter
public final class WorkDone {
	
	private final int workDoneNumber;

	public WorkDone(final int newWorkDone) {
		this.workDoneNumber = newWorkDone;
	}

	@Override
	public String toString() {
		return this.workDoneNumber+"";
	}
}
