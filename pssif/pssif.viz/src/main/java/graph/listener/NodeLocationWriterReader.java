package graph.listener;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import graph.model.IMyNode;
import graph.model.MyEdge;
import graph.model.MyNode;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class NodeLocationWriterReader {
	final String splitter = ",";
	public void saveNodesToFile(String filename, Layout<IMyNode, MyEdge> layout)
	{
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			for (IMyNode mn : layout.getGraph().getVertices())
			{
				bw.write(mn.getName() + splitter + layout.transform(mn).getX() + splitter + layout.transform(mn).getY());
				bw.newLine();
			}
			bw.close();
		}catch(Exception e){System.out.println("Cannot write to file!");}
	}
	
	public Layout<IMyNode, MyEdge> loadNodesFromFile(String filename, Layout<IMyNode, MyEdge> layout)
	{
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line=br.readLine()) != null)
			{
				String[] tokens = line.split(splitter);
				MyNode mn = this.findNode(tokens[0], layout.getGraph());
				if (mn != null)
				{
					Point2D p = new Point2D.Double(Double.valueOf(tokens[1]), Double.valueOf(tokens[2]));
					layout.setLocation(mn, p);
				}
			}
			br.close();
			return layout;
		} catch(Exception e){System.out.println("Cannot read from file!");}
		return null;
	}
	
	private MyNode findNode(String name, Graph<IMyNode, MyEdge> g)
	{
		for (IMyNode mn : g.getVertices())
			if (mn.getName().equals(name))
				return (MyNode) mn;
		return null;
		
	}
}
