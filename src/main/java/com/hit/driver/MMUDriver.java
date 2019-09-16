package com.hit.driver;

import com.hit.controller.MMUController;
import com.hit.model.MMUModel;
import com.hit.driver.CLI;
import com.hit.view.MMUView;

public class MMUDriver {

	public MMUDriver(){}

	static void	start(String[] command) throws ClassNotFoundException 
	{
		
	}

	public static void main(String[] args) {

		CLI cli = new CLI(System.in , System.out);
		MMUModel model = new MMUModel();
		MMUView view = new MMUView();
		MMUController controller = new MMUController(model, view);
		model.addObserver(controller);
		cli.addObserver(controller);
		view.addObserver(controller);
		new Thread(cli).start();
	}
}