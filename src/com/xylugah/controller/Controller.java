package com.xylugah.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.xylugah.main.Main;
import com.xylugah.model.Elevator;
import com.xylugah.model.Request;

public class Controller {
	private final int countFlors;
	private final int countElevators;
	private Queue<Request> requestQueue = new LinkedList<Request>();
	private	Map<Integer, Request> inProccessRequests = new ConcurrentHashMap<Integer, Request>();
	private AtomicInteger[] executedRequests;
	
	
	public Controller(final int countFlors, final int countElevators){
		this.countFlors = countFlors;
		this.countElevators = countElevators;
		executedRequests = new AtomicInteger[countFlors];
		for (int i = 0; i < countFlors; i++) {
			executedRequests[i] = new AtomicInteger(0);
		}

	}

	
	public int getCountFlors() {
		return countFlors;
	}

	
	public int getCountElevators() {
		return countElevators;
	}

	
	public Map<Integer, Request> getInProccessRequests() {
		return inProccessRequests;
	}


	public AtomicInteger[] getArrayServiceRequest() {
		return executedRequests;
	}


	public void setRequest(final Request request){
		synchronized(Main.lock){
			requestQueue.add(request);
			Main.lock.notifyAll();
		}
	}
	
	public Request getRequest (final int idElevator){
		synchronized(Main.lock){
			if(requestQueue.isEmpty()){
				try {
					Main.lock.wait();
				} catch (InterruptedException e) {
					System.err.println(e);
				}
			}else{
				Request a = requestQueue.remove();
				inProccessRequests.put(idElevator, a);
				return a;	
			}
		}
		return null;
		
	}
	
	public void processingRequest(final int idElevator){
		inProccessRequests.remove(idElevator);
	}
	
	public void executeRequest(final Request request){
		final int i = request.getTargetFlor();
		executedRequests[i].incrementAndGet();

	}
}
