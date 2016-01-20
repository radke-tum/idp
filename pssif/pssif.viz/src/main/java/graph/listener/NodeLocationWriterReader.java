package graph.listener;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import graph.model.IMyNode;
import graph.model.MyEdge;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import org.apache.commons.collections15.Transformer;

import de.tum.pssif.core.common.PSSIFConstants;

public class NodeLocationWriterReader {
	final String splitter = ",";
	public void saveNodesToFile(String filename, Layout<IMyNode, MyEdge> layout)
	{
		filename = createNodePositionFileName(filename);
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			for (IMyNode mn : layout.getGraph().getVertices())
			{
				bw.write(mn.getNode().getId()
						+ splitter + layout.transform(mn).getX() + splitter + layout.transform(mn).getY());
				bw.newLine();
			}
			bw.close();
		}catch(Exception e){System.out.println("Cannot save node positions!");}
	}
	
	private String createNodePositionFileName(String filename) {
		//creating the filename for saving the node positions
		filename = filename.substring(0, filename.lastIndexOf("."));
		int index = filename.lastIndexOf(File.separator);
		filename = filename.substring(index+1);
		filename = PSSIFConstants.NODE_POSITION_FOLDER_PATH + filename;
		return filename;
	}

	public Layout<IMyNode, MyEdge> loadNodesFromFile(String filename, Graph<IMyNode, MyEdge> g, Dimension dimension)
	{
		
		if (filename == null || filename.isEmpty())
			return null;
		filename = createNodePositionFileName(filename);
		final HashMap<IMyNode, Point2D> positions = new HashMap<IMyNode, Point2D>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line=br.readLine()) != null)
			{
				String[] tokens = line.split(splitter);
				IMyNode mn = this.findNode(tokens[0], g);
				if (mn != null)
				{
					Point2D p = new Point2D.Double(Double.valueOf(tokens[1]), Double.valueOf(tokens[2]));
					positions.put(mn, p);
				}
			}
			br.close();
			
			
			if (positions.size() < g.getVertices().size())
				return null;
			return new EmptyLayout<IMyNode, MyEdge>(g, createNodePositionTransform(positions), dimension);
			
		} catch(Exception e){System.out.println("Cannot load node positions!");}
		return null;
	}


	private Transformer<IMyNode, Point2D> createNodePositionTransform(final HashMap<IMyNode, Point2D> positions) {
		return new Transformer<IMyNode, Point2D>() {
			
			@Override
			public Point2D transform(IMyNode arg0) {
				return positions.get(arg0);
			}
		};
	}

	private IMyNode findNode(String id, Graph<IMyNode, MyEdge> g)
	{
		
		for (IMyNode mn : g.getVertices())
			if (mn.getNode().getId().trim().equals(id.trim()))
				return mn;
		return null;
		
	}

}
