package org.pssif.consistencyExtern.consistencyGui;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import org.pssif.comparedDataStructures.ComparedNodePair;

/**
This file is part of PSSIF Consistency. It is responsible for keeping consistency between different requirements models or versions of models.
Copyright (C) 2014 Andreas Genz

    PSSIF Consistency is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PSSIF Consistency is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with PSSIF Consistency.  If not, see <http://www.gnu.org/licenses/>.
    
    Feel free to contact me via eMail: genz@in.tum.de
*/

/**
 * This class is an own implementation of the class AbstractTable Model. It is
 * used to display the possible match candidates in a JTable in the dialogue
 * after the match metrics were all calculated.
 * 
 * @author Andreas
 * 
 */
@SuppressWarnings("serial")
public class MatchCandidateTableModel extends AbstractTableModel {

	/**
	 * The names of the column of the table
	 */
	private static final String[] COLUMN_NAMES = { "Node Origin", "Node New",
			"Syntactic Sim", "Semantic Sim", "Context Sim", "Overall", "Link" };

	/**
	 * indexes for the columns
	 */
	private static final int COLUMN_IDX_NODEORIGIN = 0;
	private static final int COLUMN_IDX_NODENEW = 1;
	private static final int COLUMN_IDX_SYNTACTICSIM = 2;
	private static final int COLUMN_IDX_SEMANTICSIM = 3;
	private static final int COLUMN_IDX_CONTEXTSIM = 4;
	private static final int COLUMN_IDX_OVERALL = 5;
	private static final int COLUMN_IDX_MERGE = 6;

	/**
	 * the table data
	 */
	private final List<ComparedNodePair> matchCandidates;

	/**
	 * the types of the columns
	 */
	private static final Class<?>[] COLUMN_CLASSES = { String.class,
			String.class, Double.class, Double.class, Double.class, Double.class,
			Boolean.class };

	public MatchCandidateTableModel(final List<ComparedNodePair> matchCandidates) {
		this.matchCandidates = matchCandidates;
	}

	@Override
	public int getRowCount() {
		return matchCandidates.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public String getColumnName(final int columnIndex) {
		return (COLUMN_NAMES[columnIndex]);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final ComparedNodePair comparedNodePair = matchCandidates.get(rowIndex);

		if (columnIndex == COLUMN_IDX_NODEORIGIN) {
			return comparedNodePair.getLabelComparison().getLabelOrigin();
		} else if (columnIndex == COLUMN_IDX_NODENEW) {
			return comparedNodePair.getLabelComparison().getLabelNew();
		} else if (columnIndex == COLUMN_IDX_SYNTACTICSIM) {
			return comparedNodePair.getWeightedSyntacticResult();
		} else if (columnIndex == COLUMN_IDX_SEMANTICSIM) {
			return comparedNodePair.getWeightedSemanticResult();
		} else if (columnIndex == COLUMN_IDX_CONTEXTSIM) {
			return comparedNodePair.getWeightedContextResult();
		} else if (columnIndex == COLUMN_IDX_OVERALL) {
			return (comparedNodePair.getWeightedContextResult()+comparedNodePair.getWeightedSemanticResult()+comparedNodePair.getWeightedSyntacticResult())/3;
		} else if (columnIndex == COLUMN_IDX_MERGE) {
			return comparedNodePair.isEquals();
		}

		throw new IllegalArgumentException("Invalid column index "
				+ columnIndex);
	}

	
	public void sort(int col, boolean increasing) { // Sort and notify listeners
		for (int i = 0; i < this.getRowCount(); i++)
		{
			for (int j = 0; j < this.getRowCount()-1; j++)
			{
				if (increasing)
				{
					if (compare(this.getValueAt(j, col),this.getValueAt(j+1, col)))
						swapRows(j, j+1);
				}
				else
				{
					if (compare(this.getValueAt(j+1, col), this.getValueAt(j, col)))
						swapRows(j, j+1);
				}
			}
		}

	}
	
	private boolean compare(Object o1, Object o2)
	{
		if ((o1 instanceof Double) && (o2 instanceof Double))
			return ((Double) o1) > ((Double) o2);
		else if ((o1 instanceof String) && (o2 instanceof String))
		{
			String s1 = (String) o1;
			String s2 = (String) o2;
			return (s1.compareTo(s2) > 0);
		}
		else if ((o1 instanceof Boolean) && (o2 instanceof Boolean))
		{
			boolean b1 = (Boolean) o1;
			boolean b2 = (Boolean) o2;
			return (b1 && !b2);
		}
		else
			return false;
	}
	
	public void swapRows(int firstRow, int secondRow)
	{
		if (firstRow == secondRow)
			return;
		int min = firstRow;
		int max = secondRow;
		if (firstRow > secondRow)
		{
			min = secondRow;
			max = firstRow;
		}
		
		ComparedNodePair tmp;
		tmp = matchCandidates.get(min);
		matchCandidates.remove(min);
		matchCandidates.add(max, tmp);
		tmp = matchCandidates.get(max-1);
		matchCandidates.remove(max-1);
		matchCandidates.add(min, tmp);
		
		fireTableDataChanged();
	}
	
	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return COLUMN_CLASSES[columnIndex];
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return (columnIndex == COLUMN_IDX_MERGE);
	}

	@Override
	public void setValueAt(final Object value, final int rowIndex,
			final int columnIndex) {
		final ComparedNodePair comparedNodePair = matchCandidates.get(rowIndex);

		if (columnIndex == COLUMN_IDX_MERGE) {
			comparedNodePair.setEquals((Boolean) value);
			comparedNodePair.getLabelComparison().setEquals((Boolean) value);
			comparedNodePair.getTokensComparison().setEquals((Boolean) value);
		}

		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/**
	 * This method takes a JTable and adjusts the column widths to make the
	 * looking of the result of the matching process nicer.
	 * 
	 * @param table
	 *            the table which columns shall be initialized
	 */
	public static void initColumnWidths(final JTable table) {
		final TableColumnModel tableColumnModel = table.getColumnModel();

		tableColumnModel.getColumn(COLUMN_IDX_NODEORIGIN).setMinWidth(250);
		tableColumnModel.getColumn(COLUMN_IDX_NODENEW).setMinWidth(250);
		tableColumnModel.getColumn(COLUMN_IDX_SYNTACTICSIM).setPreferredWidth(
				95);
		tableColumnModel.getColumn(COLUMN_IDX_SEMANTICSIM)
				.setPreferredWidth(95);
		tableColumnModel.getColumn(COLUMN_IDX_CONTEXTSIM).setPreferredWidth(80);
		tableColumnModel.getColumn(COLUMN_IDX_MERGE).setPreferredWidth(60);
	}

}
