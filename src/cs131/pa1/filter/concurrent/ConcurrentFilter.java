package cs131.pa1.filter.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected BlockingQueue<String> input; //LinkedBlockingQueue that stores the input Strings
	protected BlockingQueue<String> output; //LinkedBlockingQueue that stores the output Strings
	protected final String POISON_PILL = "WAKE_UP_TREAD"; //String that represents the flag in output

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
	}
	
	//process() method is now concurrent rather than sequential 
	public void process(){
		while (true) {
			try {
				//using take() rather than poll() because there is no predefined waiting time, 
				//will wait until new input is available, otherwise go to sleep 
				String line = input.take();
				if (line.equals(this.POISON_PILL)) {
					output.add(this.POISON_PILL);
					break;
				}
				String processedLine = processLine(line);
				if (processedLine != null) {
					output.add(processedLine);
				} 
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	//determine the current thread running right now, and check whether
	//it has terminated
	@Override
	public boolean isDone() {
		Thread current = Thread.currentThread();
		return current.getState().equals(Thread.State.TERMINATED);
	}
	
	protected abstract String processLine(String line);

}
