package cs131.pa1.filter.concurrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cs131.pa1.filter.Message;

public class CatFilter extends ConcurrentFilter{
	private Scanner reader;
	/**
	 * This class creates the Cat Filter that reads a file and stores content in output
	 * @param line: command String of the filter
	 * @throws Exception
	 */
	public CatFilter(String line) throws Exception {
		super();
		
		//parsing the cat options
		String[] args = line.split(" ");
		String filename;
		//obviously incorrect number of parameters
		if(args.length == 1) {
			System.out.printf(Message.REQUIRES_PARAMETER.toString(), line);
			throw new Exception();
		} else {
			try {
				filename = args[1];
			} catch (Exception e) {
				System.out.printf(Message.REQUIRES_PARAMETER.toString(), line);
				throw new Exception();
			}
		}
		try {
			reader = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.printf(Message.FILE_NOT_FOUND.toString(), line);
			throw new FileNotFoundException();
		}
	}

	@Override
	public void run() {
		process();
	}
	
	@Override
	public void process() {
		//continues while there's more lines in file
		while(reader.hasNext()) {
			String processedLine = processLine("");
			if(processedLine == null) {
				break;
			}
			output.add(processedLine);
		}
		reader.close();
		
		//add POISON_PILL to notify next filter that there will
		//be no more input
		output.add(this.POISON_PILL);
	}
	
	@Override
	public String processLine(String line) {
		if(reader.hasNextLine()) {
			return reader.nextLine();
		} else {
			return null;
		}
	}
}
