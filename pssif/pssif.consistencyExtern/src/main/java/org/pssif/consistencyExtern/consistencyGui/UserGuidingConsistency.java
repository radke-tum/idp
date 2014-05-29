package org.pssif.consistencyExtern.consistencyGui;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.pssif.mainProcesses.CompairsonProcess;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.matchingLogic.MatchingMethods;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;

/**
 * @author Andreas TODO
 */
public class UserGuidingConsistency {

	public static void main(Model activeModel, Model newModel,
			Metamodel metaModel) {

		List<MatchMethod> matchMethods = openChooseMatchingMethodsPopup();

		CompairsonProcess.main(activeModel, newModel, metaModel, matchMethods);
	}

	/**
	 * @author: Andreas
	 * @return a boolean that says whether the user wants to merge the newly
	 *         imported model with the old one
	 */
	public static boolean openConsistencyPopUp() {

		boolean result = false;

		Object[] options = { "Merge new into existing", "Copy new to existing" };
		int n = JOptionPane
				.showOptionDialog(
						null,
						"You have imported a second model into the integration framework.\n \n Would you like to merge the original and the newly imported model into one?",
						"Merge Models", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		switch (n) {
		case JOptionPane.YES_OPTION:
			result = true;
			break;
		case JOptionPane.NO_OPTION:
			result = false;
			break;
		}

		return result;
	}

	/**
	 * @author: Andreas
	 * @return the set of matchMethods which shall be applied to the data
	 */
	private static List<MatchMethod> openChooseMatchingMethodsPopup() {

		// TODO: Open Dialog here and ask the user which metrics he wants
		List<MatchMethod> result = chooseMatchingMethodsPopup();

		return result;
	}

	private static List<MatchMethod> chooseMatchingMethodsPopup() {

		final JFrame frame = new JFrame("Match method selection");
		
		List<MatchMethod> methods = new LinkedList<MatchMethod>();
		MatchingMethods[] allMethods = MatchingMethods.values();
		
		for(MatchingMethods matchMethod : allMethods){
			methods.add(MatchMethod.createMatchMethodObject(matchMethod, false, 0.0));
		}
		
		final TableModel tableModel = new MethodChooseTableModel(methods);
		
		JTable methodTable = new JTable(tableModel);
		methodTable.setAutoCreateColumnsFromModel(true);
		methodTable.setAutoCreateRowSorter(true);
		
		frame.add(new JScrollPane(methodTable), BorderLayout.CENTER);
		frame.setSize(500,150);
		frame.setVisible(true);
		
		
		return methods;
	}

}
