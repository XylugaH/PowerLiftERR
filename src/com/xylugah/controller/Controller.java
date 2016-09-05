package com.xylugah.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xylugah.main.Main;
import com.xylugah.model.Elevator;
import com.xylugah.model.Request;

public class Controller {
	private final int countFlors;
	private final int countElevators;
	private final List<Elevator> arrayOfElevators = new CopyOnWriteArrayList<Elevator>();
	private Queue<Request> requestQueue = new LinkedList<Request>();
	private	Map<Integer, Request> inProccessRequests = new ConcurrentHashMap<Integer, Request>();
	private List<Request> executedRequests = new CopyOnWriteArrayList<Request>();
	
	
	public Controller(final int countFlors, final int countElevators){
		this.countFlors = countFlors;
		this.countElevators = countElevators;
		for (int i = 0; i < countElevators; i++) {
			arrayOfElevators.add(new Elevator(i,this));
		}
	}

	
	public int getCountFlors() {
		return countFlors;
	}

	
	public int getCountElevators() {
		return countElevators;
	}


	public List<Elevator> getarrayOfElevators(){
		return this.arrayOfElevators;
	}
	
	
	public Map<Integer, Request> getInProccessRequests() {
		return inProccessRequests;
	}


	public List<Request> getArrayServiceRequest() {
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
		arrayOfElevators.get(idElevator).setFull(true);
	}
	
	public void executeRequest(final int idElevator){
			executedRequests.add(arrayOfElevators.get(idElevator).getRequest());
			arrayOfElevators.get(idElevator).setFull(false);
			arrayOfElevators.get(idElevator).setRequest(null);
	}
}
