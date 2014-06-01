package graph.model;

<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

=======
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
import de.tum.pssif.core.metamodel.PrimitiveDataType;
=======
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
import de.tum.pssif.core.model.Node;

public class MyJunctionNode implements IMyNode{
	
	private Node node;
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
	private double sizeheight;
	private double sizewidth;
=======
	//private double sizeheight;
	//private double sizewidth;
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
	private MyJunctionNodeType type;
	private boolean detailedOutput;
	private boolean visible;
	
	//private static int limit = 5;
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
	private static int lineLimit = 18;
=======
	//private static int lineLimit = 18;
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
	
	public MyJunctionNode(Node node, MyJunctionNodeType type) {
		this.node = node;
		this.type = type;
		this.visible = true;
	}
	
	/**
	 * Pretty printed Name
	 * @return a html name
	 */
		public String getName()
		{
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
			String res = findName().replaceAll("&lt;", "<");
			res = res.replaceAll("<br>", "");
			
			return res;
=======
			//String res = findName().replaceAll("&lt;", "<");
			//res = res.replaceAll("<br>", "");
			
			//return findName();
			return type.getName();
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		}
		
		/**
		 * Actual name value
		 * @return the name
		 */
		public String getRealName()
		{
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
			return findName();
=======
			//return findName();
			return type.getName();
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		}
		
		/**
		 * Get the name from the Node object
		 * @return the actual name or "Name not available" if the name was not defined
		 */
		private String findName()
		{
			String name="Name not available";
			// find the name of the Node
			PSSIFOption<Attribute> tmp =type.getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
			if (tmp.isOne())
			{
				Attribute nodeName = tmp.getOne();
				
				if (nodeName.get(node)!=null)
				{
					PSSIFValue value =null;
					if (nodeName.get(node).isOne())
					{
						value = nodeName.get(node).getOne();
						name = value.asString();
					}
					if (nodeName.get(node).isNone())
					{
						name ="Name not available";
					}
				}
				else
					name ="Name not available";
			}
				
			return name;
		}

		public MyJunctionNodeType getNodeType() {
			return type;
		}

		public boolean isDetailedOutput() {
			return detailedOutput;
		}
		
		/**
		 * Defines if all the attributes and additional information are shown in the GUI
		 * @param detailedOutput
		 */
		public void setDetailedOutput(boolean detailedOutput) {
			this.detailedOutput = detailedOutput;
		}

		public Node getNode() {
			return node;
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}
		/**
		 * Get all the Informations about the Node. Should only be used in the GraphVisualization
		 * @param details true show all details, false only name
		 * @return a HTML String with all the node informations
		 */
		public String getNodeInformations(boolean details)
		{
			String output="";
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
			if (details)
=======
			/*if (details)
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
			{
				output ="<table border=\"0\">";
				output+=" <tr> ";
				output+= "<th> <h3>&lt;&lt; "+type.getName()+" &gt;&gt; <br>";
				output+= evalName(findName());
				output+= "</h3> </th> </tr>";
				output+=" <tr> ";
				output+= "<td> <b>Attributes</b></td>";
				output+=  " </tr> ";
				for (String s : calcAttr())
				{
					output+=" <tr> ";
					output+= "<td> "+s+" </td>";
					output+=  " </tr> ";
				}
				
				output+=" </table>";
			}
			else
			{
				output+="<h3>&lt;&lt; "+type.getName()+" &gt;&gt; <br>"+ evalName(findName())+"</h3>";
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
			}
=======
			}*/
			output = "<h3>&lt;&lt; "+type.getName()+" &gt;&gt; <br>"+ findName()+"</h3>";
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
			
			return output;
		}
		
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		private String evalName(String name)
=======
		/*private String evalName(String name)
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		{
			//String name = findName();
			
			if (name.length()>lineLimit)
			{
				String res;
				List<Integer> spaceIndexes = getSpaceIndexes(name);
				
				int previous =-1;
				for (int current : spaceIndexes)
				{
					if (current > lineLimit)
					{
						if (previous!=-1)
						{
							res = name.substring(0, previous)+" <br>";
							
							return res+evalName(name.substring(previous+1));
						}
						else
						{
							res = name.substring(0, lineLimit-1)+"- <br>";
							
							return res+evalName(name.substring(lineLimit));
						}						
					}
					else
					{
						previous=current;
					}
				}
				
				if (previous==-1)
				{
					res = name.substring(0, lineLimit-1)+"- <br>";
					
					return res+evalName(name.substring(lineLimit));
				}
				
				if (previous<=lineLimit)
				{
					res = name.substring(0, previous)+" <br>";
					
					return res+name.substring(previous+1);
				}
			}

			return name+" ";
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		}
		
		private int nameLines(String name)
=======
		}*/
		
	/*	private int nameLines(String name)
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		{
			if (name.length()>lineLimit)
			{
				List<Integer> spaceIndexes = getSpaceIndexes(name);
				
				int previous =-1;
				for (int current : spaceIndexes)
				{
					if (current > lineLimit)
					{
						if (previous!=-1)
						{
							return 1+nameLines(name.substring(previous+1));
						}
						else
						{
							return 1+nameLines(name.substring(lineLimit));
						}						
					}
					else
					{
						previous=current;
					}
				}
				
				if (previous==-1)
				{
					return 1+nameLines(name.substring(lineLimit));
				}
				
				if (previous<=lineLimit)
				{
					return 2;
				}
			}

			return 1;
		}
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		
		public List<Integer> getSpaceIndexes(String value)
=======
		*/
	/*	public List<Integer> getSpaceIndexes(String value)
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		{
			int position = 0;
			
			List<Integer> res = new LinkedList<Integer>();
			
			int space = value.indexOf(" ");
			while (space !=-1)
			{
				position = position+space;
				
				res.add(position);
				
				value = value.substring(space+1);
				position++;
				
				space = value.indexOf(" ");
				
			}
			
			return res;
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		}
=======
		}*/
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		
		/**
		 * Get all the Attributes from this node
		 * @return List with the attributes. Format : Name = Value in (Unit) Datatype 
		 */
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		private List<String> calcAttr()
=======
		/*private List<String> calcAttr()
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		{
			List<String> attributes = new LinkedList<String>();
			
			PSSIFOption<Attribute> tmp = type.getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
			Attribute nodeName;
			if (tmp.isOne())
				nodeName = tmp.getOne();
			else
				nodeName =null;
			
			Collection<Attribute> attr = type.getType().getAttributes();
			
			for (Attribute current : attr)
			{
				if (!current.equals(nodeName))
				{
					String attrName = current.getName();
					
					PSSIFValue value=null;
					
					if (current.get(node)!=null && current.get(node).isOne())
						value = current.get(node).getOne();
					
					String attrValue="";
					if (value !=null)
					{
						if (((PrimitiveDataType)current.getType()).getName().equals("Date"))
						{
							DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
							attrValue= df.format(value.getValue());
						}
						else
							attrValue = String.valueOf(value.getValue());
					}
					
					String attrUnit = current.getUnit().getName();
					
					String res;
					
					if (attrUnit.equals("none"))
						res = attrName+" = "+attrValue;//+" : "+((PrimitiveDataType)current.getType()).getName();
					else
						res = attrName+" = "+attrValue+" in "+attrUnit;//+ " : "+((PrimitiveDataType)current.getType()).getName();
					
					if (current.get(node)!=null && attrValue.length()>0)
						attributes.add(res);
				}
			}
			
			return attributes;
<<<<<<< HEAD:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		}
=======
		}*/
>>>>>>> origin/attempt4:pssif/pssif.viz/src/main/java/graph/model/MyJunctionNode.java
		
		

}
