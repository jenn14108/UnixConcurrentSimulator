package cs131.pa1.filter.concurrent;

/**
 * This class creates the Pwd Filter that display the currentWorkingDirectory
 * @throws Exception
 */
public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	@Override
	public void process() {
		//add to output the current working directory, then signal that the command
		//has been completed by adding the poison_pill into the output
		output.add(processLine(""));
		output.add(this.POISON_PILL);
	}
	
	@Override
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}
}
