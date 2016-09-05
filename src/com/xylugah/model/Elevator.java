package com.xylugah.model;

import com.xylugah.controller.Controller;

public class Elevator implements Runnable{
		private final long speed = 10L;
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
		
		public Elevator(final int id, final Controller c){
			this.thread = new Thread(this);
			this.id = id;
			this.currentFlor = 1;
			this.controller = c;
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
					currentFlor-=1;
				}else{
					currentFlor+=1;					
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
					move(getRequest().getTargetFlor());
					controller.executeRequest(this.id);
				}
				
			}
		}
}
