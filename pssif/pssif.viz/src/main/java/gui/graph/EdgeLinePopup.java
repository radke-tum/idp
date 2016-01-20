package gui.graph;

import edu.uci.ics.jung.visualization.RenderContext;
import graph.model.MyEdgeType;
import gui.GraphView;
import gui.enhancement.LineSpec;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.ModelBuilder;

public class EdgeLinePopup {

	private JPanel panel;
	private JList<MyEdgeType> edgeTypeList;
	private HashMap<MyEdgeType, LineSpec> lineMapper;
	private GraphView graphView;
	private MyEdgeType selected = null;
	
	public Color theColor = Color.BLACK;
	public int theThick = 1;
	public float[] theType = null;
	
	JRadioButton straight;
	JRadioButton dashed;
	JRadioButton dotted;
	final JTextField ttext = new JTextField("  ");
	final JLabel colorsample = new JLabel("THE COLOR");
	
	public void setSelected(MyEdgeType type)
	{
		selected = type;
	}
	
	public EdgeLinePopup(GraphView graphView) {
		this.graphView = graphView;
		this.lineMapper = graphView.getGraph().getEdgeLineMapping();
	}

	private void evalDialog (int dialogResult)
	{
		if (dialogResult==JOptionPane.OK_OPTION)
	 	{
	 		graphView.getGraph().setEdgeLineMapping(lineMapper);
	 	}
	}
	
	public void showPopup() {
		panel = createPanel();
		int dialogResult = JOptionPane.showConfirmDialog(null, panel, "Choose Edge Line", JOptionPane.OK_CANCEL_OPTION);
		
		evalDialog(dialogResult);
		
	}
	
	private JPanel createPanel() {
		JPanel bannerPanel = new JPanel(new BorderLayout());
		MyEdgeType[] edgetypes = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();
		
		edgeTypeList = new JList<MyEdgeType>( edgetypes );  
	    edgeTypeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    ListSelectionHandler lsh = new ListSelectionHandler(this);
	    edgeTypeList.addListSelectionListener(lsh);
	     
	    JScrollPane sp = new JScrollPane( edgeTypeList ); 
	    bannerPanel.add(sp, BorderLayout.WEST);
	    
	    JPanel specP = new JPanel();
	    specP.setLayout(new BoxLayout(specP, BoxLayout.Y_AXIS));
	    
	    //color panel
		final JColorChooser cchooser = new JColorChooser();
	    JButton choosec = new JButton("Choose Color");
	    choosec.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog( cchooser, "Color Chooser", Color.BLACK );
				colorsample.setForeground(color);
	            colorsample.repaint();
	            theColor = color;
	            	
			}
		});
	    JPanel colorP = new JPanel();
	    colorP.add(choosec);
	    colorP.add(colorsample);
	    
	    //thickness panel
	    JPanel tpanel = new JPanel();
	    JLabel tlabel = new JLabel("Thickness:");
	    tpanel.add(tlabel, BorderLayout.WEST);
	    ttext.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) { updateThickness(); }
			
			@Override
			public void insertUpdate(DocumentEvent e) { updateThickness(); }
			
			@Override
			public void changedUpdate(DocumentEvent e) { updateThickness(); }
		});
	    tpanel.add(ttext, BorderLayout.EAST);
	    
	    //type panel
	    JPanel typepanel = new JPanel();
	    typepanel.setLayout(new BoxLayout(typepanel, BoxLayout.X_AXIS));
	    ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try{
					JRadioButton b = (JRadioButton) e.getSource();
					if (b.isSelected())
					{
						switch(b.getText())
						{
						case "Straight":
							theType=null;
							break;
						case "Dashed":
							theType=RenderContext.dashing;
							break;
						case "Dotted":
							theType=RenderContext.dotting;
							break;
						}
					}
				} catch(Exception e2){}
			}
		};
	    straight = new JRadioButton("Straight");
	    straight.addChangeListener(cl);
	    dashed = new JRadioButton("Dashed");
	    dashed.addChangeListener(cl);
	    dotted = new JRadioButton("Dotted");
	    dotted.addChangeListener(cl);
	    //JRadioButton customized = new JRadioButton("Customized");
	    ButtonGroup typegroup = new ButtonGroup();
	    typegroup.add(straight);
	    typegroup.add(dashed);
	    typegroup.add(dotted);
	    //typegroup.add(customized);
	    typepanel.add(straight);
	    typepanel.add(dashed);
	    typepanel.add(dotted);
	    //typepanel.add(customized);
	    straight.setSelected(true);
	    
	    colorP.setAlignmentX(Component.CENTER_ALIGNMENT);
	    specP.add(colorP);
	    tpanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    specP.add(tpanel);
	    typepanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    specP.add(typepanel);
	    JButton applyB = new JButton("                 Apply                 ");
	    applyB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected == null)
					return;
				LineSpec tmp = new LineSpec();
				tmp.setColor(theColor);
				tmp.setThickness(theThick);
				tmp.setType(theType);
				lineMapper.put(selected, tmp);
			}
		});
	    applyB.setAlignmentX(Component.CENTER_ALIGNMENT);
	    specP.add(applyB);
	    specP.setBorder(BorderFactory.createTitledBorder("Line Style"));
	    
	    bannerPanel.add(specP, BorderLayout.EAST);
	    bannerPanel.setSize(100, 400);
	    return bannerPanel;
	}
	
	public void updateData()
	{
		if (selected == null)
			return;
		LineSpec tmp = lineMapper.get(selected);
		if (tmp == null)
		{
			colorsample.setForeground(Color.BLACK);
			ttext.setText("1");
			straight.setSelected(true);
			return;
		}
		colorsample.setForeground(tmp.getColor());
		theThick = tmp.getThickness();
		ttext.setText(theThick+"");
		if (tmp.getType() == null)
			straight.setSelected(true);
		else if (tmp.getType() == RenderContext.dashing)
			dashed.setSelected(true);
		else if (tmp.getType() == RenderContext.dotting)
			dotted.setSelected(true);
	}
	
	private void updateThickness()
	{
		try{
			theThick = Integer.valueOf(ttext.getText().trim());
		}catch(Exception e){theThick = 1;}
	}
	

}
