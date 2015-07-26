package gui.graph;

 
import graph.model.MyNodeType;
import gui.MainFrame;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
 

@SuppressWarnings("serial")
public class ImageImporter extends JPanel
                             implements ActionListener {
	
	public static int IMG_WIDTH = 128;
	public static int IMG_HEIGHT = 128;
	
	ImageIcon icon;
	
    private JButton openButton;
    private JButton remButton;
    private JButton chgButton;
    private JFileChooser fc;
    private File file ;
    private JLabel picLabel = new JLabel();
    private NodeIconPopup nsp;
    private MyNodeType currentNode;
    private JPanel buttonPanel = new JPanel();
    
    public ImageImporter(NodeIconPopup nsp) {
         
        this.nsp = nsp;
        //Create a file chooser
        fc = new JFileChooser();
 
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
 
        
        setLayout(new BorderLayout());
        openButton = new JButton("Select an Image File...");
        openButton.addActionListener(this);
        
        remButton = new JButton("Remove Icon");
        remButton.addActionListener(this);
 
        chgButton = new JButton("Change Size");
        chgButton.addActionListener(this);
        
        //For layout purposes, put the buttons in a separate panel
         //use FlowLayout
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(openButton);
        buttonPanel.add(remButton);
        buttonPanel.add(chgButton);
       
        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
   //     add(logScrollPane, BorderLayout.CENTER);

    }
 
    public void actionPerformed(ActionEvent e) {
    	  //Handle open button action.
        if (e.getSource() == openButton) {
        	handleShapeMapping();
        } 
        else if (e.getSource() == remButton)
        {
        	nsp.getIconMapper().remove(currentNode);
        	this.wipeImage();
        }
        else if (e.getSource() == chgButton)
        {
        	handleShapeSizeChange();
        }

    }
    
    private void handleShapeSizeChange() {
		// TODO Auto-generated method stub
    	IconSizePopup sizepop = new IconSizePopup();
    	sizepop.showPopup();
    	IMG_WIDTH = (int) sizepop.getWidth();
    	IMG_HEIGHT = (int) sizepop.getHeight();
    	String tmpFileAdd = this.icon.getDescription();
    	ImageIcon tmpIcon = reloadIcon(this.icon);
    	tmpIcon.setDescription(tmpFileAdd);
    	nsp.getIconMapper().put(currentNode, tmpIcon);
    	showImage(tmpIcon);
		
	}
    
    private ImageIcon reloadIcon(ImageIcon icon)
    {
    	try
    	{
    		File tmpFile = new File(icon.getDescription());
    		return loadImageBySize(tmpFile, IMG_WIDTH, IMG_HEIGHT);
    	} catch (Exception e) 
    	{
    		System.out.println("inja readi refigh....");
    		return null;
    	}
    }

	public void setCurrentNode(MyNodeType node){
    	this.currentNode = node;
    }
 
    public void handleShapeMapping(){ 
    	
       	// Get array of available formats
        	String[] suffices = ImageIO.getReaderFileSuffixes();

        	// Add a file filter for each one
        	for (int i = 0; i < suffices.length; i++) {
        		FileNameExtensionFilter filter = new FileNameExtensionFilter(suffices[i] + " Files", suffices[i]);
        	    fc.addChoosableFileFilter(filter);
        	}

            int returnVal = fc.showOpenDialog(this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
           		file = fc.getSelectedFile();
           		if (picLabel != null)
           			this.remove(picLabel);
    			icon = loadImageBySize(file, IMG_WIDTH, IMG_HEIGHT);
    			//String basepath = MainFrame.INSTALL_FOLDER;
    			icon.setDescription(file.getPath());
               	nsp.getIconMapper().put(currentNode, icon);
               	this.showImage(icon);
            } 
  
        
    }
    
    public static ImageIcon loadImageBySize(File imgFile, int width, int height){
    	ImageIcon icon = null;
    	try{
    		BufferedImage originalImage = ImageIO.read(imgFile);
    		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();     
		
    		BufferedImage resizedImage = new BufferedImage(width, height, type);
    		Graphics2D g = resizedImage.createGraphics();
    		g.drawImage(originalImage, 0, 0, width, height, null);
    		g.dispose();
	 
    		icon = new ImageIcon(resizedImage);
    	}
        catch(Exception e)
        {
        	System.out.println("Cannot read the image from file.");
        }
		
    	return icon;
	}
    
    public void showImage(Icon icon){
    	if (icon == null)
    	{
    		this.wipeImage();
    		return;
    	}
    	if (picLabel != null)
    		this.remove(picLabel);
    	this.icon = (ImageIcon) icon;
    	//icon.paintIcon(this, getGraphics(), (this.getWidth()-IMG_WIDTH)/2, 30);
    	picLabel = new JLabel(icon);
    	picLabel.setSize(this.getSize());
    	this.add(picLabel, BorderLayout.CENTER);
        this.repaint();
    }
    public void wipeImage(){
    	if (picLabel != null)
    		this.remove(picLabel);
        this.repaint();
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageImporter.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}