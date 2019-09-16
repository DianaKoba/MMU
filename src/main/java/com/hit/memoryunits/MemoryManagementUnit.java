package com.hit.memoryunits;

import java.util.logging.Level;

import com.hit.algorithm.IAlgoCache;
import com.hit.util.MMULogger;

public class MemoryManagementUnit {

	private IAlgoCache<Long, Long> algo;
	private RAM ram;
	
	public int numPageFaults;
	public int numPageReplacements;
	public int numPagesRequested;

	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long,Long> algo){
		
		this.ram=new RAM(ramCapacity);
		this.algo=algo; 
		numPageFaults = 0;
		numPageReplacements = 0;
		numPagesRequested = 0;
	}
	
	public RAM getRAM() {
		return ram;
	}
	
	public IAlgoCache <Long,Long> getAlgo(){
		return algo; 
	}
	
	public void reset() {
		ram.reset();
		algo.reset();
		numPageFaults = 0;
		numPageReplacements = 0;
		numPagesRequested = 0;
	}
	
	/* The function implements the main functionality of the MMU which is to 
	 * get pages for processes requests. It tries to fetch them from RAM and if they are not found on RAM (page fault) then 
	 * It fetches them from the HDD. 
	 * It also uses the selected algorithm to decide which page in RAM should be replaced. */
	public Page<byte[]>[] getPages(Long[] pageIds) throws java.io.IOException{
		
		@SuppressWarnings("unchecked")
		Page<byte[]>[] pages=new Page[pageIds.length];
		numPagesRequested += pageIds.length;
		pages = ram.getPages(pageIds); // request pages from RAM
		for(int i=0;i<pages.length;i++)
		{
			if(pages[i]==null) //page was not found in RAM i.e. pageFault
			{	
				MMULogger.getInstance().write("PF: " + pageIds[i], Level.INFO);
				numPageFaults++;
         		if(!ram.isFull()) 
				{
         			/* Get the Page from HD */
					pages[i]=HardDisk.getInstance().pageFault(pageIds[i]);
					
					/* Add the Page to the RAM mapping */
					ram.addPage(pages[i]);
					
					/* updates the algorithm that pageFault happened for this pageID */
					algo.putElement(pageIds[i], pageIds[i]);
					
					//System.out.println("Page Fault pageID = " + pageIds[i] + " Current Ram capacity " + ram.getCurrentCapacity() + " of " + ram.getInitialCapacity());
				}
				else          
				{
					numPageReplacements++;
					/* Get the pageId to replace from RAM */
			     	long tempId = algo.putElement(pageIds[i], pageIds[i]);
			     	
			     	/* Write the page to replace into HD */
			     	Page<byte[]> pageToReplace = HardDisk.getInstance().pageFault(tempId);
					HardDisk.getInstance().pageReplacement(pageToReplace, pageIds[i]);
					
					/* get the requested page from HD*/
					Page<byte[]> requestedPage = HardDisk.getInstance().pageFault(pageIds[i]);	
					pages[i] = requestedPage;
					
					/* remove the 'replace page" from RAM */
					ram.removePage(pageToReplace);
					
					/* add the requested page to RAM */
					ram.addPage(requestedPage);
					
					MMULogger.getInstance().write("PR: MTH " + pageToReplace.getPageId() + " MTR " + pageIds[i], Level.INFO);
					//System.out.println("Page Fault pageID = " + pageIds[i] + "replaced with pageID " + pageToReplace.getPageId() + " Current Ram capacity " + ram.getCurrentCapacity() + " of " + ram.getInitialCapacity());
				}
			}
		}
		
		return pages;
	}
	
	public RAM getRam()
	{
		return ram;
	}
	
	public void setAlgo(IAlgoCache<Long, Long> algo)
	{
		this.algo = algo;
	}
	
	public void setRam(RAM ram)
	{
		this.ram = ram;
	}
	
	public void shutDown()
	{
		
	}
	
	public void update()
	{
		
	}
}
