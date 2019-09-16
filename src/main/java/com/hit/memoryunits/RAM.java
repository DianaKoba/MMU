package com.hit.memoryunits;

import java.util.HashMap;
import java.util.Map;

public class RAM {
	
	private int initialCapacity;
	private Map<Long, Page<byte[]>> pages;
	
	private static int PAGE_SIZE_IN_BYTES = 5;
	
	public RAM(int initialCapacity) {
		this.initialCapacity=initialCapacity;
		pages = new HashMap<Long, Page<byte[]>>(initialCapacity);
	}
	
	public void reset() {
		pages.clear();
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]>[] getPages(Long[] pageIds){
		
		Page<byte[]>[] result = new Page[pageIds.length];
		for (int i=0;i<pageIds.length;i++)
			result[i] = getPage(pageIds[i]);
		return result;
	}
	
	public void setPages(Map <Long, Page<byte[]>> pages){
		
		this.pages = pages;
	}
	
	public Page<byte[]> getPage(Long pageId){
		
		Page<byte[]> result = null;
		
		if (pages.containsKey(pageId)) {
			result =  pages.get(pageId);
		}
		return result;
	}
	
	public void addPage(Page<byte[]> addPage){       
		
		this.pages.put(addPage.getPageId(), addPage);
	}
	
	public void removePage(Page<byte[]> removePage){
	
		this.pages.remove(removePage);
	}
	
	public Map<Long, Page<byte[]>> getPages(){
		
		return this.pages;                         
	}
	
	public void addPages(Page<byte[]>[] addPages){   
		
		for(Page<byte[]> page : addPages)
			this.pages.put(page.getPageId(), page);
	}
	
	public void removePages(Page<byte[]>[] removePages){
		
		for(Page<byte[]> page : removePages)
			this.pages.remove(page);
	}
	
	public int getInitialCapacity(){
	
		return this.initialCapacity;
	}
	
	public void setInitialCapacity(int initialCapacity) {
	
		this.initialCapacity = initialCapacity;
	}
	
	public int getCurrentCapacity() {
		
		return pages.size();
	}
	
	public boolean isFull(){
		
		return this.initialCapacity <= pages.size();
	}
	
	/* This function is used for retrieving the RAM content. It's used by the Controller in order to 
	 * pass it to the View for showing it on the GUI.
	 * */
	public String[][] getRAMTable() {
		String table[][] = new String[6][initialCapacity];
        int i,j;
        for (i = 0; i < (PAGE_SIZE_IN_BYTES+1); ++i) {
        	for (j = 0; j < initialCapacity; ++j) {
        		if (i == 0) {
        			table[i][j] = " ";        			
        		} else {
        			table[i][j] = "0";
        		}
        	}
        }

        int curr_place = 0;
		for (Map.Entry<Long, Page<byte[]>> entry : pages.entrySet())
		{
			table[0][curr_place] = Long.toString(entry.getKey());
			Page<byte[]> page = entry.getValue();
			for (i = 0; i < PAGE_SIZE_IN_BYTES; ++i) {
				int val = page.getContent()[i];
				table[i+1][curr_place] = Integer.toString(val);
			}
			curr_place++;
		}
		
		return table;
	}
}
