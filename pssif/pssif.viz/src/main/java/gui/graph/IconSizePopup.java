package gui.graph;

import graph.model.MyNode;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IconSizePopup extends MyPopup{

	private JPanel panel;
	JTextField height;
	JTextField width;
	MyNode selectedNode;
	boolean noNode = false;
	double widthval = 0;
	double heightval = 0;
	
	public IconSizePopup(MyNode node){
		this.selectedNode = node;
	}
	
	public IconSizePopup() {
		noNode = true;
	}

	private JPanel createPanel()
	{
		JPanel bannerPanel = new JPanel();
		height = new JTextField();
		height.setColumns(2);
		width = new JTextField();
		width.setColumns(2);
		
		bannerPanel.add(new JLabel("Height:"),BorderLayout.WEST);
		bannerPanel.add(height, BorderLayout.WEST);
		bannerPanel.add(new JLabel("Width:"),BorderLayout.EAST );
		bannerPanel.add(width,BorderLayout.EAST);
		
		return bannerPanel;
		
	}
	
	public void showPopup()
	{
		panel = createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, panel, "Customize Size of Nodes", JOptionPane.OK_CANCEL_OPTION);
		
		evalDialog(dialogResult);
	}
	
	private void evalDialog (int dialogResult)
	{
		if (dialogResult==0)
	 	{
			if (!noNode && isDigit(height.getText()) && isDigit(width.getText())){
			//	System.out.println("both valid numbers");
				selectedNode.setHeight(Double.valueOf(height.getText()));
				selectedNode.setWidth(Double.valueOf(width.getText()));
				//selectedNode.update();
				System.out.println(selectedNode.getName() + ":" + selectedNode.getWidth() + ":" + selectedNode.getHeight());
			}
			else if (noNode && isDigit(height.getText()) && isDigit(width.getText())){
				widthval = Double.valueOf(width.getText());
				heightval = Double.valueOf(height.getText());
			}
	 	}
	}
    private boolean isDigit (String string) {
        for (int n = 0; n < string.length(); n++) {
            char c = string.charAt(n);//get a single character of the string
            //System.out.println(c);
            if (!Character.isDigit(c)) {//if its an alphabetic character or white space
                return false;
            }
        }
        return true;
    }
    
    public double getWidth()
    {
    	if (noNode)
    		return widthval;
    	else
    		return selectedNode.getWidth();
    }
    
    public double getHeight()
    {
    	if (noNode)
    		return heightval;
    	else
    		return selectedNode.getHeight();
    }

}

