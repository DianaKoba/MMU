package com.hit.processes;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import com.hit.memoryunits.HardDisk;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;
import com.hit.util.MMULogger;

public class Process implements Callable<Boolean> {
	
	private int id;
	private MemoryManagementUnit mmu;
	private ProcessCycles processCycles;
	private int curCommandIdx;
	private int curInCommandIdx;
	private boolean isSelected;

	public Process (int id, MemoryManagementUnit mmu, ProcessCycles processCycles){
		
		this.id = id;
		this.mmu = mmu;
		this.processCycles = processCycles;
		curCommandIdx = 0;
		curInCommandIdx = 0;
		isSelected = false;
	}
	
	public void reset() {
		curCommandIdx = 0;
		curInCommandIdx = 0;
	}
	
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public int getId(){
		
		return id;
	}
	
	public void setId(int id){
		
		this.id = id;
	}
	
	public ProcessCycles getProcessCycles() {
		
		return processCycles;
	}

	public Boolean call() throws Exception {

		System.out.println("Process number "+ this.id+" is started" );
		for (ProcessCycle processCycle : processCycles.getProcessCycles()) {
			List<Long> pageId = processCycle.getPages();
			List<byte[]> data = processCycle.getData();
			try	{
				synchronized (mmu)	{
					Long[] pageIdsToRequest = processCycle.getPages().toArray(new Long[processCycle.getPages().size()]);
					System.out.println("Process " + id + " request pages: " + pageId);
					Page<byte[]>[] pagesList = mmu.getPages(pageIdsToRequest); 
					for (int i=0; i<pagesList.length;i++){
						Page<byte[]> page = pagesList[i];
						if (page.getContent() == null){
							pagesList[i] = new Page<byte[]>(pageId.get(i), data.get(i));
							HardDisk.getInstance().pageReplacement(pagesList[i], pageId.get(i));
						}
					}
					Thread.sleep(processCycle.getSleepMs());					
				}				
			}catch(InterruptedException e) {
				e.printStackTrace();
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
				//System.out.println(e);
			}catch( Exception e) {
				e.printStackTrace();
				MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
				//System.out.println(e);
			}
		}
		return true;
	}
	
	
	/* if there are commands to run - run and return 'false'
	 * if there are no commands - return 'true'
	 * The function returns 'true' if there are no more commands to run 
	*/
	
	public boolean runSingleCommmand() {
		List<ProcessCycle> process_cycle_list = processCycles.getProcessCycles();
		
		if (curCommandIdx >= process_cycle_list.size()) {
			/* no more commands */
			return true;
		}
		
		ProcessCycle singleCommand = process_cycle_list.get(curCommandIdx);
		List<Long> pageIds = singleCommand.getPages();

		if (curInCommandIdx >= pageIds.size()) {
			curCommandIdx++;
			if (curCommandIdx >= process_cycle_list.size()) {
				/* no more commands */
				return true;
			}
			curInCommandIdx = 0;
			singleCommand = process_cycle_list.get(curCommandIdx);
			pageIds = singleCommand.getPages();
		}
		
		Long pageIdsToRequest[] = new Long[1];		
		pageIdsToRequest[0] = pageIds.get(curInCommandIdx);
		
		Page<byte[]>[] pagesList;
		try {
			/* Get pages from MMU */
			pagesList = mmu.getPages(pageIdsToRequest);
			
			/* Update Page data */
			Page<byte[]> page = pagesList[0];
			byte content[] = singleCommand.getData().get(curInCommandIdx);
			page.setContent(content);
			
			String contentString = "[";
			for (int i = 0; i < content.length; ++i) {
				contentString  += Integer.toString((int) content[i]);
				if (i < (content.length-1)) {
					contentString += ", ";
				}
			}
			contentString += "]";
			
			MMULogger.getInstance().write("GP: " + pageIdsToRequest[0] + " " + contentString, Level.INFO);
			
			curInCommandIdx++;

		} catch (IOException e) {
			e.printStackTrace();
			MMULogger.getInstance().write(e.getMessage(), Level.SEVERE);
		}
		
		return false;
	}
	
}
