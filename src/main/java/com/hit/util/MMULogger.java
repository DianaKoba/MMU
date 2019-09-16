package com.hit.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MMULogger {
	
	public final static String DEFAULT_FILE_NAME = "logs/log.txt";
	private FileHandler handler;
	private static MMULogger instance = null;
	
	private Logger logger; 
	
	public static MMULogger getInstance()
	{
		if (instance == null)
			instance = new MMULogger();
		return instance;
	}
	
	private MMULogger(){
		logger = Logger.getLogger(DEFAULT_FILE_NAME);
		try {
			handler = new FileHandler(DEFAULT_FILE_NAME);
		} catch (SecurityException | IOException e) {
			MMULogger.getInstance().write(e.toString(), Level.SEVERE);
			e.printStackTrace();
		}	
		
		handler.setFormatter(new OnlyMessageFormatter());
		logger.addHandler(handler);
	}
	
	public synchronized void write(String command, Level level){
		logger.log(level, command + "\n");
	}
	
	public class OnlyMessageFormatter extends Formatter {
		public OnlyMessageFormatter() {super();}
		
		public String format (final LogRecord record) {
			return record.getMessage();
			
		}
	}
}