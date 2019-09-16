package com.hit.memoryunits;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.hit.util.MMULogger;

public class HardDisk {
	
	static final String DEFAULT_FILE_NAME = "src/main/resources/hdPages.txt";
	static final int _SIZE = 1000;
	private static final HardDisk instance = new HardDisk();
	LinkedHashMap<Long,Page<byte[]>> hardDiskMap;
	
	@SuppressWarnings("unchecked")
	private HardDisk(){
		hardDiskMap = new LinkedHashMap<>(_SIZE);
		try {
			File file = new File(DEFAULT_FILE_NAME);
			if (file.exists()) {
				//System.out.println("Deleting HDD file from previous run");
				file.delete();
			}
			
			file.createNewFile();
			
			FileInputStream fis = new FileInputStream(DEFAULT_FILE_NAME);
			ObjectInputStream in = new ObjectInputStream(fis);
			try{
			    while(true){
					Page<byte[]> temp = (Page<byte[]>)in.readObject();
					hardDiskMap.put(temp.getPageId(), temp);
				}
			}
			finally{
					in.close();
					fis.close();
			}
		}
		
		catch (EOFException e) {
			// end of file
		}
		catch (ClassNotFoundException e){
			MMULogger.getInstance().write("ClassNotFoundException", Level.SEVERE);
			//System.out.println("ClassNotFoundException");
		}
		catch (FileNotFoundException e1){
			MMULogger.getInstance().write("FileNotFoundException", Level.SEVERE);
			//System.out.println("FileNotFoundException");
		}
		catch(IOException e2){
			MMULogger.getInstance().write("IOException", Level.SEVERE);
			//System.out.println("IOException");
		}
	}
	
	public static HardDisk getInstance()
	{
		return HardDisk.instance;
	}
	
	
	/* 
	 * This function gets 'pageId' and returns the Page from HD
	 * If the pageId doesn't exists, it creates a new one and adds to HD
	 */
	public Page<byte[]> pageFault(Long pageId)throws FileNotFoundException, IOException{
		
		if(!this.hardDiskMap.containsKey(pageId))
			this.hardDiskMap.put(pageId, new Page<byte[]>( pageId, new byte[]{}));
		
		Page<byte[]> temp=this.hardDiskMap.get(pageId);
		return temp;
	}
	
	/* Get 'moveToHdPage' - the page we want to move from RAM to HD
	 * Get 'moveToRamId' - the page ID we want to bring from HD to RAM
	 * The function:
	 * 	Add the 'moveToHdPage' to HD:
	 * 		- by adding a map PageID->Page
	 *  	- and writing the Page to the HD file
	 * Returns the Page with PageID 'moveToRamId' we want to read to RAM 
	 *  
	 */
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws java.io.FileNotFoundException,java.io.IOException{
		hardDiskMap.replace(moveToHdPage.getPageId(), moveToHdPage);
		writeHd();
		return pageFault(moveToRamId);
	}   
	
	private void writeHd() throws FileNotFoundException, IOException{
		ObjectOutputStream out= new ObjectOutputStream(new FileOutputStream(DEFAULT_FILE_NAME));
		for(Entry<Long, Page<byte[]>> entry : hardDiskMap.entrySet()) {	
		    Long key = entry.getKey();
		    out.writeObject(hardDiskMap.get(key));
		}
		out.close();		
	}
}
