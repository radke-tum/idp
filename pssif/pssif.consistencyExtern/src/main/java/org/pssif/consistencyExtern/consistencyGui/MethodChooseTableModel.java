package org.pssif.consistencyExtern.consistencyGui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.pssif.matchingLogic.MatchMethod;

public class MethodChooseTableModel extends AbstractTableModel{

	/**
	 * The names of the column of the table
	 */
	private static final String[] COLUMN_NAMES = { "Methodname", "Weigth", "Active" };
	
	/**
	 * indexes for the columns
	 */
	private static final int COLUMN_IDX_METHODNAME = 0;
	private static final int COLUMN_IDX_WEIGTH = 1;
	private static final int COLUMN_IDX_ACTIVE = 2;
	
	/**
	 * the table data
	 */
	private final List<MatchMethod> methods;
	
	public MethodChooseTableModel (final List<MatchMethod> methods){
		this.methods = methods;
	}
	
	@Override
	public int getRowCount() {
		return methods.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final MatchMethod method = methods.get(rowIndex);
		
		if(columnIndex == COLUMN_IDX_METHODNAME){
			return method.getMatchMethod().getDescription();
		}
		if(columnIndex == COLUMN_IDX_WEIGTH){
			return method.getWeigth();
		}
		if(columnIndex == COLUMN_IDX_ACTIVE){
			return method.isActive();
		}
		
		throw new IllegalArgumentException("Invalid column index " + columnIndex);
	}

}
