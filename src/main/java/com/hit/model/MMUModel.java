package com.hit.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import java.util.logging.Level;

import com.google.gson.Gson;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MRUAlgoCacheImpl;
import com.hit.algorithm.Random;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model{
	
	int numProcesses;
	int ramCapacity;
	IAlgoCache<Long, Long> algo;
	MemoryManagementUnit mmu;
	RunConfiguration runConfig;
	List<Process> processesList;	
	String[] configuration;

	private static String CONFIG_FILE_NAME = "src/main/resources/com/hit/config/Configuration.json";
	
	public int getNumOfProcesses()
	{
		return numProcesses;
	}
	
	public int getRamCapacity()
	{
		return ramCapacity;
	}
	
	public int getNumProcesses() {
		return processesList.size();
	}
	
	public String[][] getRAMTable() {
		return mmu.getRAM().getRAMTable();
	}
	
	public void setConfiguration(String[] configuration)
	{
		this.configuration = configuration;
	}
	
	public int getNumPageFaults() {
		return mmu.numPageFaults;
	}
	
	public int getNumPageReplacements() {
		return mmu.numPageReplacements;
	}
	
	public int getNumPagesRequested() {
		return mmu.numPagesRequested;
	}
	
	@Override
	public void start(){
		String alg = configuration[0];
		ramCapacity = Integer.parseInt(configuration[1]);
		
		if (alg.equals("LRU")) {
			algo = new LRUAlgoCacheImpl<Long, Long>(ramCapacity); 	
		} 
		else if (alg.equals("MRU")) {
				algo = new MRUAlgoCacheImpl<Long, Long>(ramCapacity); 	
		}
		else if (alg.equals("RAN")) {
				algo = new Random<Long, Long>(ramCapacity); 
		}
		
		mmu = new MemoryManagementUnit(ramCapacity, algo);
		RunConfiguration runConfig = readConfigurationFile();
		numProcesses = runConfig.getNumProcesses();
		processesList = createProcesses(runConfig.getProcessesCycles(), mmu);
		MMULogger.getInstance().write("RC: " + ramCapacity, Level.INFO);
		MMULogger.getInstance().write("PN: " + numProcesses, Level.INFO);
		setChanged();
		notifyObservers();
	}
	
	private static RunConfiguration readConfigurationFile()
	{
		FileReader configFile = null;
		try 
		{
			configFile = new FileReader(CONFIG_FILE_NAME);
		} 
		catch (FileNotFoundException exception) 
		{
			MMULogger.getInstance().write(exception.getMessage(), Level.SEVERE);
		}
		
		return new Gson().fromJson(configFile, RunConfiguration.class);
	}
	
	static List<Process> createProcesses(List<ProcessCycles> appliocationsScenarios, MemoryManagementUnit mmu){
		
		List<Process> listProcesses = new LinkedList<Process>();
		int processId = 0;
		for (ProcessCycles processCycles : appliocationsScenarios) {
			Process process = new Process(processId++, mmu, processCycles); 
			listProcesses.add(process);
		}
		return listProcesses;
	}
	
	public void updateSelectedProcesses(boolean ProcessesSelection[]) {
		
		int i = 0;
		for (Process pr : processesList) {
			pr.setIsSelected(ProcessesSelection[i]);
			i++;
		}
	}
	
	public void playAll() {
		boolean is_done;
		
		for (Process process : processesList) {
			if (!process.isSelected()) {
				continue;
			}
			
			is_done = false;
			while (!is_done) {
				is_done = process.runSingleCommmand();
			}
		}
	}
	
	public void play() {
		boolean is_done;
		
		for (Process process : processesList) {
			if (!process.isSelected()) {
				continue;
			}
			is_done = process.runSingleCommmand();
			if (is_done == false) {
				return;
			}	
		}
	}
	
	public void reset() {
		for (Process process : processesList) {
			process.reset();
		}
		mmu.reset();
	}
}

