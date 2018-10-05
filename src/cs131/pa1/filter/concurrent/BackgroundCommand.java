package cs131.pa1.filter.concurrent;

public class BackgroundCommand {
	private String command;
	private Thread thread;
	private int index;
	public BackgroundCommand(int index, String command,Thread thread) {
		this.command = command;
		this.thread = thread;
		this.index = index;
		
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public Thread getThread() {
		return this.thread;
	}
	
	public int getIndex() {
		return this.index;
	}
}
