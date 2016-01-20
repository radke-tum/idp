package org.pssif.consistencyExtern.consistencyGui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class ButtonTableHeader extends JButton implements TableCellRenderer, MouseListener {
	
	protected ButtonTableHeader rendererComponent;
	protected int column;
	protected boolean mousePressed = false;
	protected String caption;
	public ButtonTableHeader(String caption, ActionListener actionListener) {
		rendererComponent = this;
		rendererComponent.addActionListener(actionListener);
		this.caption = caption;
	}
	
	public Component getTableCellRendererComponent(
	  JTable table, Object value,
	  boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (table != null) {
		  JTableHeader header = table.getTableHeader();
		  if (header != null) {
		    rendererComponent.setForeground(header.getForeground());
		    rendererComponent.setBackground(header.getBackground());
		    rendererComponent.setFont(header.getFont());
		    header.addMouseListener(rendererComponent);
		  }
		}
		setColumn(column);
		rendererComponent.setText(caption);
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		return rendererComponent;
	}
	
	protected void setColumn(int column) {
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}
	
	protected void handleClickEvent(MouseEvent e)
	{
		if (mousePressed) 
		{
			mousePressed=false;
			JTableHeader header = (JTableHeader)(e.getSource());
			JTable tableView = header.getTable();
			TableColumnModel columnModel = tableView.getColumnModel();
			int viewColumn = columnModel.getColumnIndexAtX(e.getX());
			int column = tableView.convertColumnIndexToModel(viewColumn);
			if (viewColumn == this.column && e.getClickCount() == 1 && column != -1)
				doClick();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		  handleClickEvent(e);
		    ((JTableHeader)e.getSource()).repaint();		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		mousePressed = true;		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}