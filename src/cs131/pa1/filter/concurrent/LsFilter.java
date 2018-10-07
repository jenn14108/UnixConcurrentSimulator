package cs131.pa1.filter.concurrent;
import java.io.File;

/**
 * This class creates the Ls Filter that display files/directories in currentDirectory
 * @param line: command String of the filter
 * @throws Exception
 */
public class LsFilter extends ConcurrentFilter{
	int counter;
	File folder;
	File[] flist;
	
	public LsFilter() {
		super();
		counter = 0;
		folder = new File(ConcurrentREPL.currentWorkingDirectory);
		flist = folder.listFiles();
	}
	
	@Override
	public void run() {
		process();
	}
	
	@Override
	public void process() {
		while(counter < flist.length) {
			output.add(processLine(""));
		}
		//adds poison pill to stop the thread
		output.add(this.POISON_PILL);
	}
	
	@Override
	public String processLine(String line) {
		return flist[counter++].getName();
	}
}
