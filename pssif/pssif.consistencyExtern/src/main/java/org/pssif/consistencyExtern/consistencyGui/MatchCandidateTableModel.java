package org.pssif.consistencyExtern.consistencyGui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.matchingLogic.MatchMethod;

public class MatchCandidateTableModel extends AbstractTableModel {

	/**
	 * The names of the column of the table
	 */
	private static final String[] COLUMN_NAMES = { "Node Origin", "Node New", "Syntactic Sim", "Semantic Sim", "Context Sim",
			"Merge?" };

	/**
	 * indexes for the columns
	 */
	private static final int COLUMN_IDX_NODEORIGIN = 0;
	private static final int COLUMN_IDX_NODENEW = 1;
	private static final int COLUMN_IDX_SYNTACTICSIM = 2;
	private static final int COLUMN_IDX_SEMANTICSIM = 3;
	private static final int COLUMN_IDX_CONTEXTSIM = 4;
	private static final int COLUMN_IDX_MERGE = 5;


	/**
	 * the table data
	 */
	private final List<ComparedNodePair> matchCandidates;

	/**
	 * the types of the columns
	 */
	private static final Class<?>[] COLUMN_CLASSES = { String.class, String.class, Double.class, Double.class,
			Double.class, Boolean.class };

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
		} else if (columnIndex == COLUMN_IDX_SEMANTICSIM){
			return comparedNodePair.getWeightedSemanticResult();
		} else if (columnIndex == COLUMN_IDX_CONTEXTSIM){
			return comparedNodePair.getWeightedContextResult();
		} else if (columnIndex == COLUMN_IDX_MERGE){
			return comparedNodePair.isMerged();
		}

		throw new IllegalArgumentException("Invalid column index "
				+ columnIndex);
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return COLUMN_CLASSES[columnIndex];
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return (columnIndex > COLUMN_IDX_CONTEXTSIM);
	}

	@Override
	public void setValueAt(final Object value, final int rowIndex,
			final int columnIndex) {
		final ComparedNodePair comparedNodePair = matchCandidates.get(rowIndex);

		if (columnIndex == COLUMN_IDX_MERGE) {
			comparedNodePair.setMerged((Boolean) value);
			comparedNodePair.getLabelComparison().setMerged((Boolean) value);
			comparedNodePair.getTokensComparison().setMerged((Boolean) value);
		}

		fireTableCellUpdated(rowIndex, columnIndex);
	}

}
