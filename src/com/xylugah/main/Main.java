package com.xylugah.main;

import com.xylugah.controller.Controller;
import com.xylugah.controller.RequestGenerator;
import com.xylugah.model.Elevator;
import com.xylugah.view.Printer;

public class Main {
	public static int COUNT_FLORS = 9;
	public static int COUNT_ELEVATORS = 100;
	public static Object lock = new Object();
	
	public static void main(String[] args) throws InterruptedException {
		final Controller controller = new Controller(COUNT_FLORS, COUNT_ELEVATORS);
		final RequestGenerator p1 = new RequestGenerator(controller);
		
		final Printer printer = new Printer(controller);
	
		printer.getThread().setDaemon(true);
		printer.getThread().start();
		
		p1.getThread().start();
		
		for (Elevator elevator : controller.getarrayOfElevators()) {
			elevator.getThread().start();
		}
		
		p1.getThread().join();
		
		// Корявая остановка после окончания генерации запросов

		for (Elevator elevator : controller.getarrayOfElevators()) {
			elevator.noMoreRequest = true;
		}
		
		synchronized(controller){
			controller.notifyAll();
		}
		//

	}
}
