package cs131.pa1.filter.concurrent;

public class BackgroundCommand {
	private String command;
	private Thread thread;
	private int id;


	public BackgroundCommand(int id, String command,Thread thread) {
		this.command = command;
		this.thread = thread;
		this.id = id;

	}
	
	public String getCommand() {
		return this.command;
	}
	
	public Thread getThread() {
		return this.thread;
	}
	
	public int getId() {
		return this.id;
	}
}
