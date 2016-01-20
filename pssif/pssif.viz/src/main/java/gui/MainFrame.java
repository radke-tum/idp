package gui;

import gui.enhancement.ImagePanel;
import gui.graph.ImageImporter;
import gui.toolbars.ToolbarManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import reqtool.bus.ReqToolReqistry;

public class MainFrame {
	public static final String INSTALL_FOLDER = System.getProperty("user.dir");
	private JFrame frame;
	
	private MenuManager menuManager;
	private FileCommands fcommands;
	private ToolbarManager toolbarManager;
	private File backgroundFile;
		
	public JFrame getFrame(){return frame;}
	public MenuManager getMenuManager(){return menuManager;}
	public FileCommands getFileCommands(){return fcommands;}
	
	public MainFrame()
	{
		frame = new JFrame(
				"Trails");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuManager = new MenuManager(this);
		fcommands = new FileCommands(this);
		menuManager.update();
		fcommands.update();
		toolbarManager = new ToolbarManager();
		backgroundFile = new File(INSTALL_FOLDER + "//images//background.jpg");
			
		ReqToolReqistry.newInstance();

		initFrame();
	}

	public void initFrame() {
		frame.getContentPane().removeAll();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth() / 4;
		int height = gd.getDisplayMode().getHeight() / 4;

		width = width * 3;
		height = height * 3;

		frame.setPreferredSize(new Dimension(width, height));
		// create the Basic Menu Bar
		frame.setJMenuBar(menuManager.createFileMenu());
		ImageIcon ii = ImageImporter.loadImageBySize(backgroundFile, width, height);
		final ImagePanel allPanel = new ImagePanel(ii);
		allPanel.setBackground(Color.WHITE);
		allPanel.setSize(frame.getSize());
		allPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

		JLabel label = new JLabel("Start by importing a file");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setOpaque(true);
		label.setBackground(Color.WHITE);
		label.setFont(new Font("Times New Roman", Font.ITALIC, 27));

		frame.add(allPanel, BorderLayout.CENTER);
		frame.add(label, BorderLayout.PAGE_END);
		frame.add(toolbarManager.createStandardToolBar(fcommands), BorderLayout.PAGE_START);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		
		ImageIcon icon = new ImageIcon(INSTALL_FOLDER + "//images//icon3.png");
		frame.setIconImage(icon.getImage());
		frame.setVisible(true);
		
		frame.addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {
		    	ImageIcon ii = ImageImporter.loadImageBySize(backgroundFile, frame.getWidth(), frame.getHeight());
			    allPanel.reloadImage(ii);			    
		    }

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
