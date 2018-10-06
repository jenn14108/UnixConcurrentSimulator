package cs131.pa1.filter.concurrent;

public class BackgroundCommand {
	private String command; //stores the line of command from user
	private Thread thread; //stores the last thread of the command
	private int id; //stores the id of the object

	/**
	 * Constructor of the BackgroundCommand Class
	 * @param id: integer indicating the id of this object in the list
	 * @param command: String of command from user
	 * @param thread: Thread object that is the last thread in the list
	 */
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
	
	public void setThread(Thread t) {
		this.thread = t;
	}
}
