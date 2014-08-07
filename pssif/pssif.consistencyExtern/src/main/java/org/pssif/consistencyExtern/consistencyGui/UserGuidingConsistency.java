package org.pssif.consistencyExtern.consistencyGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.mainProcesses.CompairsonProcess;
import org.pssif.matchingLogic.MatchMethod;
import org.pssif.matchingLogic.MatchingMethods;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;

/**
 * This class is responsible for guiding the user through the whole consistency
 * process. For example asking whether the user wants to merge the models or
 * just cope the new one into the workspace. Another thing is that the class
 * lets the user select which match methods shall be applied and with which
 * weight.
 * 
 * @author Andreas
 */
public class UserGuidingConsistency {

	/**
	 * This method controls the gui of the consistency process. First the dialog
	 * to let the user choose the match methods is opened. Then the user is
	 * supposed to choose the tresholds for the syntactic, semantic and
	 * contextual metrics. Afterwards the matching process is started and the
	 * results are displayed to the user.
	 * 
	 * @param originalModel
	 *            The original model
	 * @param newModel
	 *            The newly imported model
	 * @param metaModelOriginal
	 *            the metamodel of the original model
	 * @param metaModelNew
	 *            the metamodel of the new model
	 * @return the list of node pairs which shall be merged
	 */
	public static List<ComparedNodePair> startConsistencyCheck(Model originalModel,
			Model newModel, Metamodel metaModelOriginal, Metamodel metaModelNew) {

		List<MatchMethod> matchMethods = openChooseMatchingMethodsPopup();

		openChooseTresholdsPopup();

		ConsistencyData consistencyData = CompairsonProcess.main(originalModel,
				newModel, metaModelOriginal, metaModelNew, matchMethods);

		consistencyData = openChooseMergeCandidatesPopup(consistencyData);

		List<ComparedNodePair> result = consistencyData.getEqualsList();
		
		consistencyData.resetComparedNodePairList();
		
		return result;
	}

	/**
	 * This method opens a dialog where the user can choose the different
	 * tresholds. After the user confirmed his input the inputs are validated.
	 * If the input was valid the tresholds are set in the ConsistencyData
	 * Class. Otherwise the dialog stays open.
	 */
	private static void openChooseTresholdsPopup() {

		final JDialog dialog = new JDialog();
		dialog.setLayout(new BorderLayout());
		dialog.setSize(280, 180);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);

		final JLabel labelSyn = new JLabel("Syntactic Threshold:");
		final JTextField synTresh = new JTextField();
		synTresh.setColumns(4);

		final JLabel labelSem = new JLabel("Semantic Threshold:");
		final JTextField semTresh = new JTextField();
		semTresh.setColumns(4);

		final JLabel labelCon = new JLabel("Context Threshold:");
		final JTextField conTresh = new JTextField();
		conTresh.setColumns(4);

		JButton buttonApply = new JButton("Choose these thresholds");
		buttonApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				double synTreshold = 0, semTreshold = 0, conTreshold = 0;
				boolean nonEmptyFields = true;

				if (synTresh.getText() != null) {
					synTreshold = Double.parseDouble(synTresh.getText());
				} else {
					nonEmptyFields = false;
				}
				if (semTresh.getText() != null) {
					semTreshold = Double.parseDouble(semTresh.getText());
				} else {
					nonEmptyFields = false;
				}
				if (conTresh.getText() != null) {
					conTreshold = Double.parseDouble(conTresh.getText());
				} else {
					nonEmptyFields = false;
				}

				if (nonEmptyFields
						&& tresholdsAreValid(synTreshold, semTreshold,
								conTreshold)) {
					dialog.setVisible(false);

					ConsistencyData.initThresholds(synTreshold, semTreshold,
							conTreshold);
				} else {
					dialog.setVisible(true);
				}
			}

			/**
			 * @return whether the given tresholds are valid
			 */
			private boolean tresholdsAreValid(double synTreshold,
					double semTreshold, double conTreshold) {
				boolean result = false;

				result = (synTreshold >= 0) && (semTreshold >= 0)
						&& (conTreshold >= 0);
				result = result && (synTreshold <= 1) && (semTreshold <= 1)
						&& (conTreshold <= 1);

				return result;
			}
		});

		JPanel treshholdPanel = new JPanel();
		treshholdPanel.add(labelSyn);
		treshholdPanel.add(synTresh);
		treshholdPanel.add(labelSem);
		treshholdPanel.add(semTresh);
		treshholdPanel.add(labelCon);
		treshholdPanel.add(conTresh);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(buttonApply);

		dialog.add(treshholdPanel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		dialog.setVisible(true);
	}

	/**
	 * This method shows the user the node pairs where at least one of the
	 * similarity result exceeds the treshold. Users can then choose which nodes
	 * shall be linked/merged. After Confirming this dialog the
	 * comparedNodePairs list in the consistencyData object is updated.
	 * 
	 * @param consistencyData
	 *            the consistencyData object which shall be modified by
	 *            selecting the "to be merged" nodes.
	 * @return the modified consistencyData object
	 */
	private static ConsistencyData openChooseMergeCandidatesPopup(
			ConsistencyData consistencyData) {
		final JDialog dialog = new JDialog();
		dialog.setLayout(new BorderLayout());
		dialog.setSize(900, 600);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);

		final TableModel tableModel = new MatchCandidateTableModel(
				consistencyData.getEqualsCandidateList());

		JTable methodTable = new JTable(tableModel);

		MatchCandidateTableModel.initColumnWidths(methodTable);

		methodTable.setFont(new Font("Arial", Font.PLAIN, 14));
		methodTable.setGridColor(new Color(808080));
		methodTable.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(methodTable);

		JButton buttonApply = new JButton(
				"Link the selected node pairs as 'equal'");
		buttonApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(buttonApply);

		dialog.add(scrollPane, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);

		dialog.setVisible(true);

		return consistencyData;
	}

	/**
	 * This method opens a modal dialogue where the user has to decide whether
	 * he wants to merge the two models or just copy the new one into the same
	 * workspace as the original one
	 * 
	 * @return a boolean that says whether the user wants to merge the newly
	 *         imported model with the old one or not
	 */
	public static boolean openConsistencyPopUp() {

		boolean result = false;

		Object[] options = { "Merge existing into new one", "Copy new to existing" };
		int n = JOptionPane
				.showOptionDialog(
						null,
						"You have imported a second model into the integration framework.\n \n What would you like to do with the original and the newly imported model?",
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
	 * This method creates an object for every possible match method and then
	 * gives this list to the chooseMatchingMethodsPopup() method.
	 * 
	 * @return the set of matchMethods which shall be applied to the data
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
	 * This method creates a dialogue where the user can choose the desired
	 * methods together with the preferences. Furthermore it's checked if the
	 * weights which the user entered are valid.
	 * 
	 * @param methods
	 *            the list of match method objects
	 * @return the edited list of match method objects. Now every object
	 *         contains the information if its method is active and which weigth
	 *         it has.
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
				// TODO implement
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
	 * This method builds the sum of weights for each different metric
	 * catgeories (syntactic, semantic & contextual) and looks whether they are
	 * all in the interval [0..1]. Also at least one metric has to be active to
	 * have a valid list with methods.
	 * 
	 * @param methods
	 *            the match method list with the weight information of each
	 *            method
	 * @return if the method list is valid(at least one metric is active and the
	 *         syntactic, semantic & contextual weights are in the interval
	 *         [0..1])
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
