package gui.graph;

import graph.model.MyEdgeType;
import graph.model.MyNodeType;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.JPanel;

public class MyPopup {
	
	private JPanel createPanel()
	{
		return null;
	}
	
	protected TreeMap <String,LinkedList<MyEdgeType>> sortByParentType (LinkedList<MyEdgeType> edgeTypes)
	{
		TreeMap <String,LinkedList<MyEdgeType>> res = new TreeMap <String,LinkedList<MyEdgeType>>();
		
		TreeMap <String,LinkedList<MyEdgeType>> map = new TreeMap<String,LinkedList<MyEdgeType>>();
		
		for (MyEdgeType t : edgeTypes)
		{
			String parentName;
			if (t.getType().getGeneral()!=null)
			{
				parentName = t.getType().getGeneral().getName();	
			}
			else
			{
				parentName="Other";
			}
			LinkedList<MyEdgeType> tmp = map.get(parentName);
			if (tmp == null)
				tmp = new LinkedList<MyEdgeType>();
			
			tmp.add(t);
			map.put(parentName, tmp);
		}
		
		Iterator<String> it = map.keySet().iterator();
		
		MyEdgeTypeComparator comp = new MyEdgeTypeComparator();
		
		while (it.hasNext())
		{
			String current = it.next();
			LinkedList<MyEdgeType> values = map.get(current);
			
			Collections.sort(values, comp);
			
			res.put(current, values);
		}
		
		System.out.println(res.size());
		
		return res;
	}
	
	protected class MyEdgeTypeComparator implements Comparator<MyEdgeType>
	{
	  @Override public int compare( MyEdgeType type1, MyEdgeType type2 )
	  {
	    return type1.getName().compareTo(type2.getName());
	  }
	}
	
	protected LinkedList<MyNodeType> sortNodeTypes (LinkedList<MyNodeType> types)
	{
		Collections.sort(types, new MyNodeTypeComparator());
		
		return types;
	}
	
	protected class MyNodeTypeComparator implements Comparator<MyNodeType>
	{
	  @Override public int compare( MyNodeType type1, MyNodeType type2 )
	  {
	    return type1.getName().compareTo(type2.getName());
	  }
	}
	
}
