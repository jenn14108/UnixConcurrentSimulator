package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.*;


public class ConcurrentREPL {

	static String currentWorkingDirectory;
//	static List<String> backgroundJobs = new ArrayList<>(); 
//	static List<Thread> backgroundLastThreads = new ArrayList<>();
	static Map<String,Thread> backgroundJobs = new LinkedHashMap<>();
	static String command;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);

		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if(!command.trim().equals("")) {
				if (command.equals("repl_jobs")) {
					displayJobs();
				}
				//building the filters list from the command
				List<ConcurrentFilter> filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				if (command.endsWith("&")) {
					createBackThreadExecuteFilters(filterlist);
				} else {
					createThreadExecuteFilters(filterlist);
				}	
				
				if (command.startsWith("kill")) {
					int index = command.charAt(command.length()-1)-'0';
					int i = 1; 
					for(String job: backgroundJobs.keySet()) {
						if (i == index) backgroundJobs.remove(job);
						i++;
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
		backgroundJobs.put(command,thr);
	}
	
	//prints out background jobs line by line
	public static void displayJobs() {
		for (String job: backgroundJobs.keySet()) {
			if (backgroundJobs.get(job).getState().equals(Thread.State.TERMINATED)) {
				backgroundJobs.remove(job);
			}
		}
		int i = 1; 
		for (String job: backgroundJobs.keySet()) {
			System.out.println(i + "." +" " + job);
			i++;
		}
	}

}
