package com.xylugah.model;

import com.xylugah.controller.Controller;

public class Elevator implements Runnable{
		private final long speed = 1000L;
		private final int id;
		private int currentFlor;
		private boolean isFull = false;
		private Request request;
		private final Thread thread;
		private final Controller controller;
		public boolean noMoreRequest = false;
		
		public Thread getThread(){
			return this.thread;
		}
		
		public Elevator(final int id, final Controller controller){
			this.thread = new Thread(this);
			this.id = id;
			this.currentFlor = 1;
			this.controller = controller;
			return;
		}

		public Request getRequest() {
			return request;
		}

		public void setRequest(final Request request) {
			this.request = request;
		}
		
				
		public boolean isFull() {
			return isFull;
		}

		public void setFull(final boolean isFull) {
			this.isFull = isFull;
		}

		public int getCurrentFlor() {
			return currentFlor;
		}

		public void setCurrentFlor(final int currentFlor) {
			this.currentFlor = currentFlor;
		}

		public int getId() {
			return this.id;
		}
		
		private synchronized void move(final int targetFlor){
			while (currentFlor!=targetFlor){
				try {
					wait(speed);
				} catch (InterruptedException e) {
					System.err.println(e);
					return;
				}
				if (currentFlor>targetFlor){
					currentFlor--;
				}else{
					currentFlor++;					
				}
			}
		}

		@Override
		public void run() {
			while(true){
				if(thread.isInterrupted()){
					break;
				}
				
				if (noMoreRequest){
					break;
				}
				
				if (this.request==null) {
						this.setRequest(controller.getRequest(this.id));
				}else{			
					move(request.getStartFlor());
					controller.processingRequest(this.id);
					setFull(true);
					move(getRequest().getTargetFlor());
					controller.executeRequest(this.request);
					setFull(false);
					setRequest(null);
				}
				
			}
		}
}
