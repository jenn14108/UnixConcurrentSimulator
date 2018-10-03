package cs131.pa1.filter.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected BlockingQueue<String> input;
	protected BlockingQueue<String> output;
	protected boolean isDone = false;
	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public Filter getNext() {
		return next;
	}
	
	public void run() {
		process();
		//set isDone to true after the specific command has completed execution
		isDone = true;
	}
	
	//process() method is now concurrent rather than sequential 
	public void process(){
		while (true) {
			//proceed if there are commands to be executed
			if (!input.isEmpty()) {
				try {
					//using take() rather than poll() because there is no predefined waiting time, 
					//will wait until new input is available, otherwise go to sleep 
					String line = input.take();
					String processedLine = processLine(line);
					if (processedLine != null) {
						output.add(processedLine);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			//break out of the wait and terminate if the previous command has finished 
			//executing and there is no more input to be received
			} else if (input.isEmpty() && prev.isDone()){
				isDone = true;
				break;
			}
		}
	}
	
	@Override
	public boolean isDone() {
		return isDone;
	}
	
	protected abstract String processLine(String line);
	
}
