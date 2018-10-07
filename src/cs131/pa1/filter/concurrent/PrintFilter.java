package cs131.pa1.filter.concurrent;


/**
 * This class creates the Print Filter that prints out the final output 
 * @throws Exception
 */
public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	@Override
	public void run() {
		process();
	}
	
	@Override
	public void process() {
		
		//runs as long as currentThread is not interrupted
		while(!Thread.currentThread().isInterrupted()) {
			try {
				String line = input.take();
				if (line.equals(this.POISON_PILL)) {
					break;
				} else if (line != null ) {
					processLine(line);
				} 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String processLine(String line) {
		if (!Thread.currentThread().isInterrupted()) {
			System.out.println(line);
		}
		return null;
	}
}
