package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


public class MMUView extends Observable implements View{

	private MMUViewPanel buttonPanel;
	private JTable tableRAM;
	private JScrollPane scrollPane;
	int ramCapacity;
	int numProcesses;
	String ramContent[][];
	String ramPageIds[];
	
	private JPanel mainPanel;
	private JPanel labelPane;
	private JPanel northPane;
	private JPanel southPane;
	private JTextField pageFaultText;
	private JTextField pageReplacementText;
	private JTextField pagesRequestedText;
	private JLabel pageFaultLabel;
	private JLabel NumPagesRequestedLabel;
	private JLabel pageReplacementLabel;
	
	private JPanel processesPanel;
	private JLabel processesLabel;
	private JList<Object> processesList;


	private static int PAGE_SIZE_IN_BYTES = 5; 
	
	public MMUView(){
	}
	
	public void setNumOfProcesses(int numProcesses)
	{
		this.numProcesses = numProcesses;
	}
	
	public void setRamCapacity(int ramCapacity)
	{
		this.ramCapacity = ramCapacity;
	}
	
	@Override
	public void start(){
		createAndShowGUI();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void createAndShowGUI() {
		int i,j;
        //Create and set up the window.
        JFrame frame = new JFrame("MMU Simulator");
        frame.getContentPane().setPreferredSize(new Dimension(700, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 0));
		frame.getContentPane().add(mainPanel);
		
		/* ---------------- RAM content ---------------------- */
        ramContent = new String[PAGE_SIZE_IN_BYTES][ramCapacity];
        
        for (i = 0; i < PAGE_SIZE_IN_BYTES; ++i) {
        	for (j = 0; j < ramCapacity; ++j) {
        		ramContent[i][j] = "0";
        	}
        }
        
        ramPageIds = new String[ramCapacity];
        
        for (j = 0; j < ramCapacity; ++j) {
        	ramPageIds[j] = " ";
        }
        
        
        tableRAM = new JTable(ramContent, ramPageIds);
		scrollPane = new JScrollPane(tableRAM);
		tableRAM.setPreferredScrollableViewportSize(tableRAM.getPreferredSize());
		/* ------------------------------------------------- */
		
		/* ------------ Statistics ------------------------ */
		labelPane = new JPanel(new GridLayout(0, 2));
		pageFaultLabel = new JLabel("Page Fault: ");
		labelPane.add(pageFaultLabel);
		
		pageFaultText = new JTextField("  0  ");
		pageFaultText.setHorizontalAlignment(JTextField.CENTER);
		labelPane.add(pageFaultText);

		pageReplacementLabel = new JLabel("Page Replacement: ");
		labelPane.add(pageReplacementLabel);

		pageReplacementText = new JTextField("  0  ");
		pageReplacementText.setHorizontalAlignment(JTextField.CENTER);
		labelPane.add(pageReplacementText);
		
		NumPagesRequestedLabel = new JLabel("Num Pages Requested: ");
		labelPane.add(NumPagesRequestedLabel);
		
		pagesRequestedText = new JTextField("  0  ");
		pagesRequestedText.setHorizontalAlignment(JTextField.CENTER);
		labelPane.add(pagesRequestedText);
		/* ------------------------------------------------- */
		
		northPane = new JPanel(new FlowLayout());
		northPane.add(scrollPane);
		northPane.add(labelPane);

		
		/* ------------ Buttons ---------------- */
		buttonPanel = new MMUViewPanel(this);
		
		/* ------------ Processes selection ---------------- */
		processesPanel = new JPanel(new BorderLayout());
		processesLabel = new JLabel("Processes");
		processesPanel.add(processesLabel, BorderLayout.NORTH);
		
		Object[] columns = new Object[numProcesses];
		for (i = 0; i < numProcesses; i++) {
			columns[i] = String.format("Process %d", i+1);
		}
		processesList = new JList(columns);
		processesPanel.add(processesList);
		
		/* ------------------------------------------------- */
		buttonPanel.setBorder(new EmptyBorder(0,  0,  0,  0));
		processesPanel.setBorder(new EmptyBorder(0,  300,  0,  0));
		
		/* ---------------- South Pane ---------------------- */
		southPane = new JPanel(new FlowLayout());		
		southPane.add(buttonPanel);
		southPane.add(processesPanel);

		/* ---------------- North Pane ---------------------- */
		mainPanel.add(northPane);
		mainPanel.add(southPane);
				
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
	public void updateView(String table[][], int numPageFaults, int numPageReplacements, int numPagesRequested) {
		int i,j;
		
		/* update RAM table */
        for (i = 0; i < PAGE_SIZE_IN_BYTES; ++i) {
        	for (j = 0; j < ramCapacity; ++j) {
        		ramContent[i][j] = table[i+1][j];
        	}
        }
        
        for (j = 0; j < ramCapacity; ++j) {
        	ramPageIds[j] = table[0][j];
        }
        
		tableRAM.setModel(new DefaultTableModel(ramContent, ramPageIds));
		tableRAM.setEnabled(false);
		
		
		/* update statistics */
		pageFaultText.setText(Integer.toString(numPageFaults));
		pageReplacementText.setText(Integer.toString(numPageReplacements));
		pagesRequestedText.setText(Integer.toString(numPagesRequested));
		
	}
	
	public boolean[] getProcessesSelection() {
		boolean isSelected[] = new boolean[numProcesses];
		int i;
		int processNum;
		
		for (i = 0; i < numProcesses; ++i) {
			isSelected[i] = false;
		}
		
		List<Object> selectedProcesses = processesList.getSelectedValuesList();
		
		for (Object element : selectedProcesses) {
			String selection = (String) element;
			processNum = Integer.parseInt(selection.split(" ")[1]);
			isSelected[processNum-1] = true;
		}
		
		return isSelected;
	}
	
	public void setPlayAll() {
		setChanged();
		notifyObservers("play_all_pressed");
		
	}
	
	public void setPlay() {
		setChanged();
		notifyObservers("play_pressed");
		
	}
	
	public void setReset() {
		setChanged();
		notifyObservers("reset_pressed");
		
	}
}
