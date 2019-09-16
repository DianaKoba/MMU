package com.hit.processes;

import java.util.List;

public class RunConfiguration {
	
	private List<ProcessCycles> processesCycles;
	
	RunConfiguration(List<ProcessCycles> processesCycles){
		this.processesCycles=processesCycles;
	}
	
	public List<ProcessCycles> getProcessesCycles() {
		return processesCycles;
	}
	
	public void setProcessesCycles(List<ProcessCycles> processesCycles) {
		this.processesCycles = processesCycles;
	}
	
	public String toString() {
		return "\n\t processes Cycles: " + this.processesCycles;
	}
	
	public int getNumProcesses(){
		return processesCycles.size();
	}
}
