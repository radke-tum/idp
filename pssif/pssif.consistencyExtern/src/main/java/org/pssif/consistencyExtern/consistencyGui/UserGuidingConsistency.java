package org.pssif.consistencyExtern.consistencyGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.pssif.mainProcesses.CompairsonProcess;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.matchingLogic.MatchingMethods;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;

/**
 * @author Andreas
 * 
 *         This class is responsible for guiding the user through the whole
 *         consistency process. For example asking whether the user wants to
 *         merge the models or just cope the new one into the workspace. Another
 *         thing is that the class lets the user select which match methods
 *         shall be applied and with which weight.
 */
public class UserGuidingConsistency {

	/**
	 * Initializes the desired match methods and starts the compairson process.
	 */
	public static void main(Model activeModel, Model newModel,
			Metamodel metaModel) {

		List<MatchMethod> matchMethods = openChooseMatchingMethodsPopup();

		CompairsonProcess.main(activeModel, newModel, metaModel, matchMethods);
	}

	/**
	 * @author: Andreas
	 * @return a boolean that says whether the user wants to merge the newly
	 *         imported model with the old one or not
	 * 
	 *         This method opens a modal dialogue where the user has to decide
	 *         whether he wants to merge the two models or just copy the new one
	 *         into the same workspace as the original one
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
	 * 
	 *         This method creates an object for every possible match method and
	 *         then gives this list to the chooseMatchingMethodsPopup() method.
	 */
	private static List<MatchMethod> openChooseMatchingMethodsPopup() {

		List<MatchMethod> methods = new LinkedList<MatchMethod>();
		MatchingMethods[] allMethods = MatchingMethods.values();

		for (MatchingMethods matchMethod : allMethods) {
			methods.add(MatchMethod.createMatchMethodObject(matchMethod, false,
					0.0));
		}

		List<MatchMethod> result = chooseMatchingMethodsPopup(methods);

		return result;
	}

	/**
	 * @param methods
	 *            the list of match method objects
	 * @return the edited list of match method objects. Now every object
	 *         contains the information if its method is active and which weigth
	 *         it has.
	 * 
	 *         This method creates a dialogue where the user can choose the
	 *         desired methods together with the preferences. Furthermore it's
	 *         checked if the weights which the user entered are valid.
	 */
	private static List<MatchMethod> chooseMatchingMethodsPopup(
			final List<MatchMethod> methods) {

		final JDialog dialog = new JDialog();
		dialog.setLayout(new BorderLayout());
		dialog.setSize(700, 400);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setResizable(false);

		final TableModel tableModel = new MethodChooseTableModel(methods);

		JTable methodTable = new JTable(tableModel);

		methodTable.setFont(new Font("Arial", Font.PLAIN, 14));
		methodTable.setGridColor(new Color(808080));
		methodTable.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(methodTable);

		JButton buttonApply = new JButton("Apply the selected merge Methods");
		buttonApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (chosenMethodsAreValid(methods)) {
					dialog.setVisible(false);
				} else {
					// TODO alert the user that metric weights have to be in the
					// interval between [0..1];
					dialog.setVisible(false);
					chooseMatchingMethodsPopup(methods);
				}
			}

		});

		JButton buttonCancel = new JButton("Cancel the merge");
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// dialog.setVisible(false);
				throw new RuntimeException(
						"Unhandled Case!! Cancel merge not yet implemented");
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(buttonApply);
		buttonPanel.add(buttonCancel);

		dialog.add(scrollPane, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		dialog.setVisible(true);

		return methods;
	}

	/**
	 * @param methods
	 *            the match method list with the weight information of each
	 *            method
	 * @return if the method list is valid(at least one metric is active and the
	 *         syntactic, semantic & contextual weights are in the interval
	 *         [0..1])
	 * 
	 *         This method builds the sum of weights for each different metric
	 *         catgeories (syntactic, semantic & contextual) and looks whether
	 *         they are all in the interval [0..1]. Also at least one metric has
	 *         to be active to have a valid list with methods.
	 */
	protected static boolean chosenMethodsAreValid(List<MatchMethod> methods) {
		boolean result;

		double syntacticWeight = 0;
		double semanticWeight = 0;
		double contextualWeight = 0;

		Iterator<MatchMethod> matchMethod = methods.iterator();
		MatchMethod method;
		boolean atLeastOneMetricSelected = false;

		while (matchMethod.hasNext()) {
			method = matchMethod.next();

			if (method.isActive()) {
				atLeastOneMetricSelected = true;

				switch (method.getMatchMethod()) {
				case EXACT_STRING_MATCHING:
				case DEPTH_MATCHING:
				case STRING_EDIT_DISTANCE_MATCHING:
				case HYPHEN_MATCHING:
					syntacticWeight += method.getWeigth();
					break;
				case LINGUISTIC_MATCHING:
				case VECTOR_SPACE_MODEL_MATCHING:
				case LATENT_SEMANTIC_INDEXING_MATCHING:
				case ATTRIBUTE_MATCHING:
					semanticWeight += method.getWeigth();
					break;
				case CONTEXT_MATCHING:
					contextualWeight += method.getWeigth();
					break;
				default:
					throw new RuntimeException("Invalid metrics were chosen. "
							+ method.getMatchMethod()
							+ " Please choose correct metrics!");
				}
			}
		}

		result = atLeastOneMetricSelected;
		result = result && (syntacticWeight >= 0) && (syntacticWeight <= 1);
		result = result && (semanticWeight >= 0) && (semanticWeight <= 1);
		result = result && (contextualWeight >= 0) && (contextualWeight <= 1);

		return result;
	}
}
