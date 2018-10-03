package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	@Override
	public void run() {
		process();
		isDone = true;
	}
	
	@Override
	public void process() {
		while(true) {
			try {
				String line = input.take();
				if (line != null ) {
					processLine(line);
				} else {
					break;
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
