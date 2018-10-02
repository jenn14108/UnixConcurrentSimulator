package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	static List<String> backgroundJobs = new ArrayList<>(); 
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
					backgroundJobs.add(command);
					createThreadExecuteFilters(filterlist);
					backgroundJobs.remove(command);
				} else {
					createThreadExecuteFilters(filterlist);
				}	
				
				if (command.startsWith("kill")) {
					int index = command.charAt(command.length()-1)-'0';
					backgroundJobs.remove(index);
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
	
	public static void displayJobs() {
		for (int i = 0; i < backgroundJobs.size(); i++) {
			System.out.println(i+1 + "." +" " + backgroundJobs.get(i));
		}
	}

}
