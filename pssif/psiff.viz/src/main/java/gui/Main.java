package gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import model.Model;

public class Main {
	
	private static MatrixView matrix;
	private static JFrame frame;
	private static GraphView graphView;
	
	public static void main(String[] args) {
		
		Model m = new Model();
		m.MockData();

		matrix = new MatrixView();
		graphView = new GraphView();

		
		
		frame = new JFrame("PSS-IF");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Standart start with Graph
		frame.getContentPane().add(graphView.getGraphPanel());
		
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setJMenuBar(createMenu());		
		frame.pack();
		frame.setVisible(true);
	}
	
	private static JMenuBar createMenu()
	{
		// PICKING or TRANSFORMING Mode
		JMenuBar menuBar = new JMenuBar();
		JMenu modeMenu = graphView.getGraph().getAbstractModalGraphMouse().getModeMenu(); // Obtain mode menu from the mouse
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null); 
		modeMenu.setPreferredSize(new Dimension(80,20));
		
		// Which Visualization
		JMenu visualisationMenu = new JMenu("Visualisation");
		JMenuItem graphItem = new JMenuItem("Graph");
		graphItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				frame.getContentPane().add(graphView.getGraphPanel());
				
				frame.pack();
				//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.repaint();
			}
		});
		visualisationMenu.add(graphItem);
		visualisationMenu.setIcon(null);
		
		
		JMenuItem matrixItem = new JMenuItem("Matrix");
		matrixItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				frame.getContentPane().add(matrix.getVisualization());
				
				
				frame.pack();
				//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.repaint();
			}
		});
		visualisationMenu.add(matrixItem);
		visualisationMenu.setIcon(null);
		
		
		// Reset the Graph or Matrix
		JMenu resetMenu = new JMenu("Reset");
		JMenuItem resetGraph = new JMenuItem("Reset Graph");
		resetGraph.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.resetGraph();
			}
		});
		resetMenu.add(resetGraph);
		JMenuItem resetMatrix = new JMenuItem("Reset Matrix");
		resetMatrix.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean res = matrix.chooseNodes();
				
				if (res)
				{
					frame.getContentPane().removeAll();
					frame.getContentPane().add(matrix.getVisualization());
					
					
					frame.pack();
					//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
					frame.repaint();
				}
			}
		});
		resetMenu.add(resetMatrix);
		resetMenu.setIcon(null);
		
		// Add all to the menuBar
		menuBar.add(modeMenu);
		menuBar.add(visualisationMenu);
		menuBar.add(resetMenu);
		
		return menuBar;
	}
}
