package cs131.pa1.filter.concurrent;

/**
 * This class creates the Wc Filter that display word count of input
 * @throws Exception
 */
public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	@Override
	public void run() {
		process();
	}
	
	@Override
	public void process() {
		while (true) {
			try {
				//using take() rather than poll() because there is no predefined waiting time, 
				//will wait until new input is available, otherwise go to sleep 
				String line = input.take();
				if (line.equals(this.POISON_PILL)) {
					//if the next line is equal to the poison pill, we know that the word count 
					//does not need to continue. Add to output and break out of while loop
					output.add(linecount + " " + wordcount + " " + charcount);
					output.add(this.POISON_PILL);
					break;
				}
				String processedLine = processLine(line);
				if (processedLine != null) {
					output.add(processedLine);
				} 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * This method simply counts the number of words, sentences, and characters in a line 
	 * of input 
	 */
	@Override
	public String processLine(String line) {
		this.linecount++;
		this.wordcount += line.split(" ").length;
		this.charcount += line.length();
		return null;
	}
}
