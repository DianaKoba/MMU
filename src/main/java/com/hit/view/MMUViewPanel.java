package com.hit.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class MMUViewPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected MMUView view;
	protected JButton play_all_button;
	protected JButton play_button;
	protected JButton reset_button;
	
	public MMUViewPanel(MMUView view) {
		this.view = view;
		
		play_button = new JButton("Play");
		play_button.setActionCommand("Play");
		play_button.addActionListener(this);
		add(play_button);
		
        play_all_button = new JButton("Play All");
        play_all_button.setActionCommand("Play All");
        play_all_button.addActionListener(this);
		add(play_all_button);
		
		reset_button = new JButton("Reset");
		reset_button.setActionCommand("Reset");
		reset_button.addActionListener(this);
		add(reset_button);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Play")) {
			view.setPlay();
		} else if (action.equals("Play All")) {	
			view.setPlayAll();
		} else {
			view.setReset();
		}
	}
}
