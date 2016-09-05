package com.xylugah.main;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xylugah.controller.Controller;
import com.xylugah.controller.RequestGenerator;
import com.xylugah.model.Elevator;
import com.xylugah.view.Printer;

public class Main {
	public static int COUNT_FLORS = 9;
	public static int COUNT_ELEVATORS = 100;
	public static Object lock = new Object();
	public static List<Elevator> arrayOfElevators = new CopyOnWriteArrayList<Elevator>();
	
	public static void main(String[] args) throws InterruptedException {
		final Controller controller = new Controller(COUNT_FLORS, COUNT_ELEVATORS);
		final RequestGenerator p1 = new RequestGenerator(controller);
		
		final Printer printer = new Printer(controller);
	
		printer.getThread().setDaemon(true);
		printer.getThread().start();
		
		p1.getThread().start();
		
		for (int i = 0; i < COUNT_ELEVATORS; i++) {
			arrayOfElevators.add(new Elevator(i, controller));
		}
		
		
		for (Elevator elevator : arrayOfElevators) {
			elevator.getThread().start();
		}
		
		p1.getThread().join();


		for (Elevator elevator : arrayOfElevators) {
			elevator.noMoreRequest = true;
		}
		
		synchronized(controller){
			controller.notifyAll();
		}
		//

	}
}
