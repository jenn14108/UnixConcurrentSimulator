package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.*;


public class ConcurrentREPL {

	static String currentWorkingDirectory;
	//a list that stores all background command objects
	static List<BackgroundCommand> backgroundJobs = new ArrayList<>(); 

	static String command;
	static int id = 1; //id of each of the background command object
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);

		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine().trim();
			if(command.equals("exit")) {
				break;
			} else if(!command.equals("")) {
				if (command.equals("repl_jobs")) {
					displayJobs();
				} else if (command.split(" ")[0].equals("kill")) {
					
					if (command.split(" ").length == 1) {
						System.out.printf(Message.REQUIRES_PARAMETER.toString(), command);
					} else if (!Character.isDigit(command.split(" ")[1].charAt(0))) {
						System.out.printf(Message.INVALID_PARAMETER.toString(), command);
					} else {
						
						int index = command.charAt(command.length()-1)-'0';
						int counter = 0; 
						//go through the background command list
						for(BackgroundCommand job: backgroundJobs) {
							//delete the job from the list if its id matches user input index
							if (index == counter+1) {
								backgroundJobs.remove(job);
								//terminate the thread of the background command job as well
								job.getThread().interrupt();
							}
							counter++;
						}
					}
				} else {
					//building the filters list from the command
					List<ConcurrentFilter> filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
					if (filterlist != null) {
						if (command.endsWith("&")) {
							createBackThreadExecuteFilters(filterlist);
						} else {
							createThreadExecuteFilters(filterlist);
						}	
					}
				}
			}
		}
		
		System.out.print(Message.GOODBYE);
	}
	
	public static void createThreadExecuteFilters(List<ConcurrentFilter> filterlist) {
		//Creating threads from the filter list and starting all threads
		Thread thr = new Thread();
		for (ConcurrentFilter filter: filterlist) {
			thr = new Thread(filter);
			thr.start();
		}
		
		//main waiting for each child thread to complete 
		try {
			thr.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
	
	public static void createBackThreadExecuteFilters(List<ConcurrentFilter> filterlist) {
		//Creating threads from the filter list and starting all threads
		Thread thr = new Thread();
		for (ConcurrentFilter filter: filterlist) {
			thr = new Thread(filter);
			thr.start();
		}
		//using last thread, create background command object and add it to list
		backgroundJobs.add(new BackgroundCommand(command,thr));
		id++;
	}
	
	//prints out background jobs line by line
	public static void displayJobs() {
		int counter = 1; 
		for (Iterator<BackgroundCommand> it = backgroundJobs.iterator(); it.hasNext(); ) {
			//create an iterator
		    BackgroundCommand job = it.next();
		    //deletes the job from the list if its thread is terminated
		    if (!job.getThread().isAlive()) {
		        it.remove();
		    } else {
		    		//print the command string out if it's still running
		    		System.out.println(counter+". " + job.getCommand());
		    		counter++;
		    		//System.out.println(" "+job.getIndex() + "." +" "+job.getCommand());
		    }
		}
	}

}
