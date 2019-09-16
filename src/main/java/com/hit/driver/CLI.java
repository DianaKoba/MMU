package com.hit.driver;
import com.hit.view.View;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Scanner;

public class CLI extends Observable implements Runnable, View {
	
	static final String START = "start"; 
	static final String STOP = "stop"; 
	static final String LRU = "LRU"; 
	static final String MRU = "MRU";
	static final String RAN = "RAN";

	
	private Scanner in;
	private PrintWriter out;
	
	public CLI(InputStream in, OutputStream out)
	{
		this.in = new Scanner(in);
		this.out = new PrintWriter(out);
	}

	
	/* This function is responsible for getting parameters from user and notifying the Controller, which in turn 
	 * passes them to the Model Later for the initialization. 
	 * */
	@Override
	public void run() {
		String config_path = null;
		String[] command = new String[3];
		String line = "";
		try {	
			while(true){
				write("Enter 'start' or 'stop':");
				String action = in.nextLine();
				
				if (action.equals(START)){
					write("Please enter required algorithm and RAM capacity:");	
					line = in.nextLine();
					String[] splitLine = line.split(" ");			
					if ((Integer.parseInt(splitLine[1])>0) && (splitLine.length==2)){
						if ((splitLine[0].equals(MRU)) || (splitLine[0].equals(LRU)) || (splitLine[0].equals(RAN))){
							command[0] = splitLine[0];
							command[1] = splitLine[1];
							command[2] = config_path;
							
							setChanged();
							notifyObservers(command);
							
							//MMUDriver.start(command);
						}else{
							write("Not a valid command 1." + splitLine[0]);
						}
					}else{
						write("Not a valid command.");
					}
				}else if (action.equals(STOP)){
					write("Thank you");
					write("Bye Bye");
					break;
				} else if (action.startsWith("LOAD")) {
					String[] splitLine = action.split(" ");
					config_path = splitLine[1];
				}
				else {
					write("Not a valid command");
					write("try again"); 
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			in.close();
			out.close();
		}
	}
	
	public void write(String string)
	{
		out.println(string);
		out.flush();
	}
	
	@Override
	public void start() 
	{
	}
}
