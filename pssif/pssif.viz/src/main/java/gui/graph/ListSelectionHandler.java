package gui.graph;

import graph.model.MyEdgeType;
import graph.model.MyNodeType;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListSelectionHandler implements ListSelectionListener  {
	
	NodeIconPopup nsp = null;
	EdgeLinePopup elp = null;
	
	public ListSelectionHandler(NodeIconPopup nsp){
		this.nsp = nsp;
	}

	public ListSelectionHandler(EdgeLinePopup edgeLinePopup) {
		elp = edgeLinePopup;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (nsp != null)
		{
			@SuppressWarnings("unchecked")
			JList<MyNodeType> nodeList = (JList<MyNodeType>)e.getSource();
			MyNodeType selectedNode = nodeList.getSelectedValue();
			nsp.ii.setCurrentNode(selectedNode);
		
			if(nsp.getIconMapper().containsKey(selectedNode))
				nsp.ii.showImage(nsp.getIconMapper().get(selectedNode));
			else
				nsp.ii.wipeImage();
		}
		
		if (elp != null)
		{
			@SuppressWarnings("unchecked")
			JList<MyEdgeType> nodeList = (JList<MyEdgeType>)e.getSource();
			MyEdgeType selectedEdge = nodeList.getSelectedValue();
			elp.setSelected(selectedEdge);
			elp.updateData();
		
			//if(nsp.getIconMapper().containsKey(selectedNode))
//				nsp.ii.showImage(nsp.getIconMapper().get(selectedNode));
			//else
			//	nsp.ii.wipeImage();
		
		}
	}
	

}
