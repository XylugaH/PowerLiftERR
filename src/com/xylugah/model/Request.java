package com.xylugah.model;

public class Request {
	private int startFlor;
	private int targetFlor;
	private int requiestID;
	
	public Request(final int startFlor,final int targetFlor,final int requiestID){
		this.startFlor = startFlor;
		this.targetFlor = targetFlor;
		this.requiestID = requiestID;		
	}
	
	public int getRequiestID() {
		return requiestID;
	}

	public void setRequiestID(final int requiestID) {
		this.requiestID = requiestID;
	}

	public int getStartFlor() {
		return startFlor;
	}
	public void setStartFlor(final int startFlor) {
		this.startFlor = startFlor;
	}
	public int getTargetFlor() {
		return targetFlor;
	}
	public void setTargetFlor(final int targetFlor) {
		this.targetFlor = targetFlor;
	}
}
