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
	/**
	 * This method returns the command String of the Object
	 * @return command
	 */
	public String getCommand() {
		return this.command;
	}
	
	/**
	 * This method returns the Thread of the Object
	 * @return thread
	 */
	public Thread getThread() {
		return this.thread;
	}
	
	/**
	 * This method returns the integer Id of the Object
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * This method sets the thread field as the thread passed in
	 * @param new Thread Object
	 */
	public void setThread(Thread t) {
		this.thread = t;
	}
}
