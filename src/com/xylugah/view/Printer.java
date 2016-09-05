package com.xylugah.view;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.xylugah.controller.Controller;
import com.xylugah.main.Main;
import com.xylugah.model.Elevator;
import com.xylugah.model.Request;

public class Printer implements Runnable{
	private final long timeGeneration = 500L;
	private Controller controller;
	private Thread thread;
	
	public Printer(Controller controller){
		this.controller = controller;
		this.thread = new Thread(this);
	}
	
	public Thread getThread(){
		return this.thread;
	}
	
	private void print(){
		List<Elevator> elevators = Main.arrayOfElevators;
		Map<Integer, Request> arrayWaitingRequest = controller.getInProccessRequests();
		AtomicInteger[] arrayServiceRequest = controller.getArrayServiceRequest();
		
		for (int i = controller.getCountFlors(); i > 0; i--) {
			int x = 0;
			for(Map.Entry<Integer, Request> e : arrayWaitingRequest.entrySet()) {
				if(e.getValue().getStartFlor()==i){
					x +=1;
				}
	        }
			System.out.print(x);
			System.out.print("[");
			for (Elevator elevator : elevators) {
				if(elevator.getCurrentFlor() == i){
					if(elevator.isFull()){
						System.out.print("H");
					}else{
						System.out.print("0");
					}
				}else{
					System.out.print("_");
				}
				System.out.print(" ");
			}
			System.out.print("]");	

			System.out.print(arrayServiceRequest[i].get());
			
			System.out.println();
		}
		System.out.println();
	}
	
	
	@Override
	public void run() {
		while (true) {
			if(thread.isInterrupted()){
				break;
			}
			
			print();
			
			try{
				Thread.sleep(timeGeneration);
			}catch(InterruptedException e){
				System.err.println(e);
			}			
		}
		
	}

}
