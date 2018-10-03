package cs131.pa1.filter.concurrent;

public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	@Override 
	public void run() {
		process();
		isDone = true;
	}
	
	@Override
	public void process() {
		output.add(processLine(""));
	}
	
	@Override
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}
}
