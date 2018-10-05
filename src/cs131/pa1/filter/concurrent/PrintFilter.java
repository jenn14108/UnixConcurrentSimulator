package cs131.pa1.filter.concurrent;

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
		System.out.println(line);
		return null;
	}
}
