package reqtool.graph;

import graph.model.MyNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import reqtool.VersionManager;
import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;

public class VersionManagerPopup {
	public static final int RESULT_OK = 1;
	public static final int RESULT_NOK = 0;
	public static final int RESULT_CANCEL = -1;
	
	public static boolean showPopup(MyNode node) {
		Collection<Attribute> nodeAttributes = node.getNodeType().getType().getAttributes();
		
    	JComponent[] inputs = new JComponent[nodeAttributes.size()*2];
		HashMap<Attribute, JTextField> attrTextFields = new HashMap<Attribute, JTextField>();
		int i=0;
		Iterator<Attribute> iterator = nodeAttributes.iterator();
		while (iterator.hasNext()) {
			Attribute attr = iterator.next();
			if (!attr.getName().equals(PSSIFConstants.BUILTIN_ATTRIBUTE_ID) && !attr.getName().equals(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME)){
				JTextField attrText = new JTextField();
				inputs[i] = new JLabel(attr.getName().toString());
				i++;
				inputs[i] = attrText;
				attrTextFields.put(attr, attrText);
				i++;
			}
		}
		
    	JTextField VersionText = new JTextField();
    	JComponent[] inputsFrr = new JComponent[] {
    			new JLabel("Version"),
    			VersionText,
    	};
    	
    	JOptionPane.showMessageDialog(null, inputs, "Create new version node", JOptionPane.PLAIN_MESSAGE);
    	// check if the user filled all the input field
    	PSSIFOption<PSSIFValue> version2 = node.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne().get(node.getNode());
		 
    	for (Entry<Attribute, JTextField> input:attrTextFields.entrySet()) {
        	if (  input.getKey().getName().equals(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION)  &&
        			input.getValue().getText()!=null && input.getValue().getText().length()>0 && !version2.isNone() 
        			&& Double.parseDouble(version2.getOne().asString()) < (Double.parseDouble(input.getValue().getText())) )
        	{
        		VersionManager vm = new VersionManager(node);
        		return vm.createNewVersion(input.getValue().getText());
        	}          
    	}
    	return false;
	}

}
