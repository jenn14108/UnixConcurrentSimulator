package cs131.pa1.filter.concurrent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import cs131.pa1.filter.Filter;
import cs131.pa1.filter.Message;

public class RedirectFilter extends ConcurrentFilter {
	private FileWriter fw;
	
	
	public RedirectFilter(String line) throws Exception {
		super();
		String[] param = line.split(">");
		if(param.length > 1) {
			if(param[1].trim().equals("")) {
				System.out.printf(Message.REQUIRES_PARAMETER.toString(), line.trim());
				throw new Exception();
			} else if (param[1].contains("&")) {
				param[1] = param[1].substring(0, param[1].lastIndexOf('&'));
			}
			try {
				fw = new FileWriter(new File(ConcurrentREPL.currentWorkingDirectory + Filter.FILE_SEPARATOR + param[1].trim()));
			} catch (IOException e) {
				System.out.printf(Message.FILE_NOT_FOUND.toString(), line);	//shouldn't really happen but just in case
				throw new Exception();
			}
		} else {
			System.out.printf(Message.REQUIRES_INPUT.toString(), line);
			throw new Exception();
		}
	}
	
	@Override
	public void process() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				//break out of the wait and terminate if the previous command has finished 
				//executing and there is no more input to be received
				if (input.isEmpty() && prev.isDone()){
					break;
				}
				//using take() rather than poll() because there is no predefined waiting time, 
				//will wait until new input is available, otherwise go to sleep 
				String line = input.take();
				processLine(line);
				if (line.equals(this.POISON_PILL)) {
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String processLine(String line) {
		try {
			if (!line.equals(this.POISON_PILL)) {
				fw.append(line + "\n");
			} else {
				fw.flush();
				fw.close();
			}
		} catch (IOException e) {
			System.out.printf(Message.FILE_NOT_FOUND.toString(), line);
		}
		return null;
	}
}
