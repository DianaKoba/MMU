package com.hit.controller;
import com.hit.driver.CLI;

import java.util.Observable;
import java.util.Observer;
import com.hit.model.MMUModel;
import com.hit.view.MMUView;


public class MMUController implements Controller, Observer {
	
	MMUModel model;
	MMUView view;
	
	public MMUController(MMUModel model, MMUView view){
		
		this.model = model;
		this.view = view;
	}
	
	/* The main function which connected the model the the view.
	 * It's being notified by CLI part of the View layer with the user input parameters
	 * Also being notified by the GUI part of the View layer when user presses buttons
	 */
	public void update(Observable o, Object arg){
		
		if(o instanceof CLI) 
		{
			model.setConfiguration((String [])arg);
			model.start();
		} 
		else if(o instanceof MMUModel) 
		{
			view.setNumOfProcesses(model.getNumProcesses());
			view.setRamCapacity(model.getRamCapacity());
			view.start();
		}else if (o instanceof MMUView){
			String view_request = (String) arg;
			boolean processesSelection[] = view.getProcessesSelection();
			model.updateSelectedProcesses(processesSelection);
			
			if (view_request.equals("play_all_pressed")) {
				model.playAll();
			} else if (view_request.equals("play_pressed")) {
				model.play();
			} else if (view_request.equals("reset_pressed")) {
				model.reset();
			}
			String table[][] = model.getRAMTable();
			view.updateView(table, model.getNumPageFaults(), model.getNumPageReplacements(), model.getNumPagesRequested());
		}
	}
}
