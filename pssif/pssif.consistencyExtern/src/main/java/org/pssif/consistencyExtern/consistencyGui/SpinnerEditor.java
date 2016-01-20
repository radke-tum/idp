package org.pssif.consistencyExtern.consistencyGui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class SpinnerEditor extends AbstractCellEditor implements TableCellEditor{
	final JSpinner spinner;
	public SpinnerEditor() {
		spinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, 0.1));
	}
	  
	@Override
	
	
	public Object getCellEditorValue() {
		return spinner.getValue();
	}

	@Override
	public Component getTableCellEditorComponent(JTable arg0, Object value, boolean arg2, int arg3, int arg4) {
		spinner.setValue(value);
	    return spinner;
	}
	

}
