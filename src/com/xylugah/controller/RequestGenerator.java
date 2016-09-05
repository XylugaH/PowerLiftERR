package com.xylugah.controller;

import java.util.Random;

import com.xylugah.model.Request;

public class RequestGenerator implements Runnable{
	private final long TIME_GENERATION = 2000L; 
	private final int COUNT_OF_REQUESTS = 2000;
	private int requestID = 0;
	private final Thread thread;
	private final Controller controller;
	
	
	public RequestGenerator(final Controller c){
		this.thread = new Thread(this);
		this.controller = c;
	}
	
	public Thread getThread(){
		return this.thread;
	}
	
	private Request getRandomRequest(){
		Random r = new Random(System.currentTimeMillis());
		int currentFlor = 1+r.nextInt(controller.getCountFlors());
		int targetFlor = currentFlor;
		while (targetFlor==currentFlor){
			targetFlor = 1+r.nextInt(controller.getCountFlors());
		}
		requestID += 1; 
		Request request = new Request(currentFlor, targetFlor, requestID);
		return request;
		
	}
	
	@Override
	public void run() {
		for (int i = 0; i < COUNT_OF_REQUESTS; i++) {
			controller.setRequest(getRandomRequest());
			try {
				Thread.sleep(TIME_GENERATION);
			} catch (InterruptedException e) {
				System.err.print(e);
			}
		}
		
	}
}
