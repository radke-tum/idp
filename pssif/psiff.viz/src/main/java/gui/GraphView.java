package gui;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.ModelBuilder;


public class GraphView {
  private JPanel             parent;
  private GraphVisualization graph;

  private JPanel             nodeInformationPanel;
  private JPanel             edgeInformationPanel;
  private JPanel             nodeAttributePanel;
  private JPanel             edgeAttributePanel;
  private JPanel             informationPanel;
  private JLabel             nodename;
  private JLabel             nodetype;
  private JLabel             edgetype;
  private JLabel             edgeSource;
  private JLabel             edgeDestination;
  private JCheckBox          nodeDetails;
  private JButton            collapseExpand;
  private boolean            active;
  private JTable             tableNodeAttributes;
  private DefaultTableModel  nodeAttributesModel;
  private JTable             tableEdgeAttributes;
  private DefaultTableModel  edgeAttributesModel;
  private ButtonGroup        group;
  private JRadioButton       nodeSelection;
  private JRadioButton       edgeSelection;
  private ItemListener       nodeListener;
  private ItemListener       edgeListener;
  private JSpinner           depthSpinner;

  private Dimension          screenSize;
  private static int         betweenComps = 7;
  private static Color       bgColor      = Color.LIGHT_GRAY;

  public GraphView(/*Dimension parentDimension*/) {
    active = false;
    screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //screenSize = parentDimension;
    //int x = (int) (screenSize.width*0.85);
    //int y = (int) (screenSize.height*0.9);
    int x = (screenSize.width);
    int y = (int) (screenSize.height * 0.78);
    if (nodeDetails == null) {
      graph = new GraphVisualization(new Dimension(x, y), true);
    }
    else {
      graph = new GraphVisualization(new Dimension(x, y), nodeDetails.isSelected());
    }

    addNodeChangeListener();
  }

  public JPanel getGraphPanel() {
    parent = new JPanel();
    parent.setLayout(new BorderLayout());

    parent.add(addGraphViz(), BorderLayout.CENTER);

    parent.add(addInformationPanel(), BorderLayout.SOUTH);

    return parent;
  }

  private JPanel addGraphViz() {
    JPanel graphpanel = new JPanel();

    VisualizationViewer<MyNode, MyEdge> vv = graph.getVisualisationViewer();

    graphpanel.add(vv);
    return graphpanel;
  }

  private JPanel addInformationPanel() {
    informationPanel = new JPanel();

    int x = (screenSize.width);
    int y = (int) (screenSize.height * 0.15);
    Dimension d = new Dimension(x, y);

    informationPanel.setMaximumSize(d);
    informationPanel.setMinimumSize(d);
    informationPanel.setPreferredSize(d);

    informationPanel.setLayout(new BorderLayout());

    // Basic Graph operations
    informationPanel.add(addBasicOperationPanel(), BorderLayout.EAST);

    // Attributs
    nodeAttributePanel = addNodeAttributePanel(x, y);
    edgeAttributePanel = addEdgeAttributePanel(x, y);

    informationPanel.add(nodeAttributePanel, BorderLayout.CENTER);

    // Basic information
    nodeInformationPanel = addNodeInformationPanel();
    edgeInformationPanel = addEdgeInformationPanel();

    informationPanel.add(nodeInformationPanel, BorderLayout.WEST);

    nodeSelection.setSelected(true);

    return informationPanel;
  }

  private JPanel addNodeInformationPanel() {
    GridBagConstraints c = new GridBagConstraints();
    int ypos = -1;

    // selected Node Information
    JPanel nodeInfos = new JPanel();
    nodeInfos.setLayout(new GridBagLayout());
    nodeInfos.setBackground(bgColor);

    c.gridx = 0;
    JLabel lblNodeName = new JLabel("Node Name");
    ypos++;
    c.gridy = ypos;
    nodeInfos.add(lblNodeName, c);

    nodename = new JLabel("");
    ypos++;
    c.gridy = ypos;
    nodeInfos.add(nodename, c);

    ypos++;
    c.gridy = ypos;
    nodeInfos.add(Box.createVerticalStrut(betweenComps), c);

    JLabel lblNodeType = new JLabel("Node Type");
    ypos++;
    c.gridy = ypos;
    nodeInfos.add(lblNodeType, c);
    nodetype = new JLabel("");
    ypos++;
    c.gridy = ypos;
    nodeInfos.add(nodetype, c);

    c.gridx = 1;
    c.gridheight = ypos;
    nodeInfos.add(Box.createHorizontalStrut(10), c);

    return nodeInfos;
  }

  private JPanel addEdgeInformationPanel() {
    GridBagConstraints c = new GridBagConstraints();
    int ypos = -1;

    // selected Node Information
    JPanel edgeInfos = new JPanel();
    edgeInfos.setLayout(new GridBagLayout());
    edgeInfos.setBackground(bgColor);

    c.gridx = 0;

    JLabel lblEdgeType = new JLabel("Edge Type");
    ypos++;
    c.gridy = ypos;
    edgeInfos.add(lblEdgeType, c);
    edgetype = new JLabel("");
    ypos++;
    c.gridy = ypos;
    edgeInfos.add(edgetype, c);

    ypos++;
    c.gridy = ypos;
    edgeInfos.add(Box.createVerticalStrut(betweenComps), c);

    JLabel lblsource = new JLabel("Source");
    ypos++;
    c.gridy = ypos;
    edgeInfos.add(lblsource, c);
    edgeSource = new JLabel("");
    ypos++;
    c.gridy = ypos;
    edgeInfos.add(edgeSource, c);

    ypos++;
    c.gridy = ypos;
    edgeInfos.add(Box.createVerticalStrut(betweenComps), c);

    JLabel lbldestination = new JLabel("Destination");
    ypos++;
    c.gridy = ypos;
    edgeInfos.add(lbldestination, c);
    edgeDestination = new JLabel("");
    ypos++;
    c.gridy = ypos;
    edgeInfos.add(edgeDestination, c);

    c.gridx = 1;
    c.gridheight = ypos;
    edgeInfos.add(Box.createHorizontalStrut(10), c);

    return edgeInfos;
  }

  private JPanel addNodeAttributePanel(int sizeX, int sizeY) {
    JPanel attributeInfos = new JPanel();
    attributeInfos.setLayout(new BorderLayout());
    attributeInfos.setBackground(bgColor);

    JLabel lblNodeAttributes = new JLabel("Node Attributes");

    attributeInfos.add(lblNodeAttributes, BorderLayout.NORTH);

    int scrollx = (int) (sizeX * 0.8);
    int scrolly = (int) (sizeY * 0.9);

    JScrollPane jScrollPane = new JScrollPane(createNodeAttributTable());
    jScrollPane.setBackground(bgColor);
    jScrollPane.setPreferredSize(new Dimension(scrollx, scrolly));
    jScrollPane.setMaximumSize(new Dimension(scrollx, scrolly));
    jScrollPane.setMinimumSize(new Dimension(scrollx, scrolly));

    attributeInfos.add(jScrollPane, BorderLayout.CENTER);

    return attributeInfos;
  }

  private JPanel addEdgeAttributePanel(int sizeX, int sizeY) {
    JPanel attributeInfos = new JPanel();
    attributeInfos.setLayout(new BorderLayout());
    attributeInfos.setBackground(bgColor);

    JLabel lblEdgeAttributes = new JLabel("Edge Attributes");

    attributeInfos.add(lblEdgeAttributes, BorderLayout.NORTH);

    int scrollx = (int) (sizeX * 0.8);
    int scrolly = (int) (sizeY * 0.9);

    JScrollPane jScrollPane = new JScrollPane(createEdgeAttributTable());
    jScrollPane.setBackground(bgColor);
    jScrollPane.setPreferredSize(new Dimension(scrollx, scrolly));
    jScrollPane.setMaximumSize(new Dimension(scrollx, scrolly));
    jScrollPane.setMinimumSize(new Dimension(scrollx, scrolly));

    attributeInfos.add(jScrollPane, BorderLayout.CENTER);

    return attributeInfos;
  }

  private JPanel addBasicOperationPanel() {
    GridBagConstraints c = new GridBagConstraints();
    int ypos = -1;

    JPanel basicOperations = new JPanel();
    basicOperations.setLayout(new GridBagLayout());
    basicOperations.setBackground(bgColor);

    JLabel pickMode = new JLabel("Pick Mode");

    edgeSelection = new JRadioButton("Edge");
    edgeSelection.setBackground(bgColor);
    edgeSelection.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        JRadioButton parent = (JRadioButton) e.getSource();
        if (parent.isSelected()) {
          addEdgeChangeListener();

          informationPanel.remove(nodeAttributePanel);
          informationPanel.remove(nodeInformationPanel);
          informationPanel.add(edgeAttributePanel, BorderLayout.CENTER);
          informationPanel.add(edgeInformationPanel, BorderLayout.WEST);

          informationPanel.validate();
          informationPanel.revalidate();
          informationPanel.repaint();

          parent.validate();
          parent.revalidate();
          parent.repaint();
        }
        else {
          System.out.println("Why Edge");
        }
      }
    });
    nodeSelection = new JRadioButton("Node");
    nodeSelection.setBackground(bgColor);
    nodeSelection.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        JRadioButton parent = (JRadioButton) e.getSource();
        if (parent.isSelected()) {
          addNodeChangeListener();

          informationPanel.remove(edgeAttributePanel);
          informationPanel.remove(edgeInformationPanel);
          informationPanel.add(nodeAttributePanel, BorderLayout.CENTER);
          informationPanel.add(nodeInformationPanel, BorderLayout.WEST);

          informationPanel.validate();
          informationPanel.revalidate();
          informationPanel.repaint();

          parent.validate();
          parent.revalidate();
          parent.repaint();
        }
      }
    });

    group = new ButtonGroup();
    group.add(edgeSelection);
    group.add(nodeSelection);

    ypos++;
    c.gridy = ypos;
    c.gridx = 1;
    JPanel pickPanel = new JPanel(new FlowLayout());
    pickPanel.setBackground(bgColor);
    pickPanel.add(pickMode);
    pickPanel.add(edgeSelection);
    pickPanel.add(nodeSelection);

    basicOperations.add(pickPanel, c);

    ypos++;
    c.gridy = ypos;
    basicOperations.add(Box.createVerticalStrut(betweenComps), c);

    JLabel lblVisDetails = new JLabel("Visualisation Details");
    ypos++;
    c.gridy = ypos;
    basicOperations.add(lblVisDetails, c);
    ypos++;
    c.gridy = ypos;
    nodeDetails = new JCheckBox("Node Details");
    nodeDetails.setBackground(bgColor);
    ypos++;
    c.gridy = ypos;
    basicOperations.add(nodeDetails, c);
    nodeDetails.setSelected(true);
    graph.setNodeVisualisation(true);
    nodeDetails.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent item) {
        JCheckBox parent = (JCheckBox) item.getSource();
        if (parent.isSelected()) {
          // checkbox selected
          graph.setNodeVisualisation(true);
        }
        else {
          // checkbox not selected
          graph.setNodeVisualisation(false);
        }
      }
    });
    ypos++;
    c.gridy = ypos;
    basicOperations.add(Box.createVerticalStrut(betweenComps), c);

    JLabel lblDepthSpinner = new JLabel("Search Depth");
    ypos++;
    c.gridy = ypos;
    basicOperations.add(lblDepthSpinner, c);

    int currentDepth = 1;
    SpinnerModel depthModel = new SpinnerNumberModel(currentDepth, //initial value
        1, //min
        currentDepth + 100, //max
        1);
    depthSpinner = new JSpinner(depthModel);

    depthSpinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {

        Object value = ((JSpinner) e.getSource()).getValue();
        if (value instanceof Integer) {
          int depth = (Integer) value;
          graph.setFollowEdgeTypes(depth);
        }

      }
    });
    ypos++;
    c.gridy = ypos;
    basicOperations.add(depthSpinner, c);

    ypos++;
    c.gridy = ypos;
    basicOperations.add(Box.createVerticalStrut(betweenComps), c);

    collapseExpand = new JButton("Collapse/Expand Node");
    collapseExpand.setEnabled(false);
    collapseExpand.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (graph.isExpandable()) {
          graph.ExpandNode(nodeDetails.isSelected());
          collapseExpand.setText("Collapse Node");
        }

        if (graph.isCollapsable()) {
          graph.collapseNode();
          collapseExpand.setText("Expand Node");
        }
      }
    });
    ypos++;
    c.gridy = ypos;
    basicOperations.add(collapseExpand, c);

    c.gridx = 0;
    c.gridheight = ypos;
    c.gridwidth = 1;
    basicOperations.add(Box.createHorizontalStrut(10), c);

    return basicOperations;
  }

  private JTable createNodeAttributTable() {
    nodeAttributesModel = new DefaultTableModel() {

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0 || columnIndex == 2 || columnIndex == 3) {
          return false;
        }
        else {
          return true;
        }
      }
    };
    nodeAttributesModel.addColumn("Attribute");
    nodeAttributesModel.addColumn("Value");
    nodeAttributesModel.addColumn("Unit");
    nodeAttributesModel.addColumn("Type");

    tableNodeAttributes = new JTable(nodeAttributesModel);
    tableNodeAttributes.getModel().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          TableModel model = (TableModel) e.getSource();

          Object data = model.getValueAt(row, column);
          String attributeName = (String) model.getValueAt(row, 0);

          // get the data to test if there was some data entered
          String testdata = (String) data;
          // do not trigger the event again if we previously wrote null back
          if (data != null && testdata.length() > 0) {
            Set<MyNode> selectedNodes = graph.getVisualisationViewer().getPickedVertexState().getPicked();
            if (!selectedNodes.isEmpty() && selectedNodes.size() == 1) {
              MyNode selectedNode = selectedNodes.iterator().next();

              boolean res = selectedNode.updateAttribute(attributeName, data);

              if (!res) {
                model.setValueAt(null, row, column);
                JPanel errorPanel = new JPanel();

                errorPanel.add(new JLabel("The value does not match the attribute data type"));

                JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        }
      }
    });

    return tableNodeAttributes;
  }

  private JTable createEdgeAttributTable() {
    edgeAttributesModel = new DefaultTableModel() {

      @Override
      public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0 || columnIndex == 2 || columnIndex == 3) {
          return false;
        }
        else {
          return true;
        }
      }
    };
    edgeAttributesModel.addColumn("Attribute");
    edgeAttributesModel.addColumn("Value");
    edgeAttributesModel.addColumn("Unit");
    edgeAttributesModel.addColumn("Type");

    tableEdgeAttributes = new JTable(edgeAttributesModel);
    tableEdgeAttributes.getModel().addTableModelListener(new TableModelListener() {

      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
          int row = e.getFirstRow();
          int column = e.getColumn();
          TableModel model = (TableModel) e.getSource();

          Object data = model.getValueAt(row, column);
          String attributeName = (String) model.getValueAt(row, 0);

          // get the data to test if there was some data entered
          String testdata = (String) data;
          // do not trigger the event again if we previously wrote null back
          if (data != null && testdata.length() > 0) {
            Set<MyEdge> selectedEdges = graph.getVisualisationViewer().getPickedEdgeState().getPicked();
            if (!selectedEdges.isEmpty() && selectedEdges.size() == 1) {
              MyEdge selectedEdge = selectedEdges.iterator().next();

              boolean res = selectedEdge.updateAttribute(attributeName, data);

              if (!res) {
                model.setValueAt(null, row, column);
                JPanel errorPanel = new JPanel();

                errorPanel.add(new JLabel("The value does not match the attribute data type"));

                JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        }
      }
    });

    return tableEdgeAttributes;
  }

  public GraphVisualization getGraph() {
    return graph;
  }

  public void resetGraph() {
    ModelBuilder.resetModel();
    this.getGraph().updateGraph();
  }

  private void updateNodeSidebar(String nodeName, MyNodeType nodeType, LinkedList<LinkedList<String>> attributes) {

    if (nodeName == null) {
      this.nodename.setText("");
    }
    else {
      this.nodename.setText(nodeName);
    }
    if (nodeType == null) {
      this.nodetype.setText("");
    }
    else {
      this.nodetype.setText(nodeType.getName());
    }
    if (attributes == null) {
      for (int i = 0; i < nodeAttributesModel.getRowCount(); i++) {
        nodeAttributesModel.removeRow(i);
      }
      nodeAttributesModel.setRowCount(0);
    }
    else {
      for (int i = 0; i < nodeAttributesModel.getRowCount(); i++) {
        nodeAttributesModel.removeRow(i);
      }
      nodeAttributesModel.setRowCount(0);

      for (LinkedList<String> currentAttr : attributes) {
        String name = currentAttr.get(0);
        String value = currentAttr.get(1);
        String unit = currentAttr.get(2);
        String type = currentAttr.get(3);
        nodeAttributesModel.addRow(new String[] { name, value, unit, type });
      }
    }

    if (graph.isCollapsable()) {
      collapseExpand.setText("Collapse Node");
    }

    if (graph.isExpandable()) {
      collapseExpand.setText("Expand Node");
    }

    if (!graph.isCollapsable() && !graph.isExpandable()) {
      collapseExpand.setText("Collapse/Expand Node");
      collapseExpand.setEnabled(false);
    }
    else {
      collapseExpand.setEnabled(true);
    }

  }

  private void updateEdgeSidebar(MyEdge edge, MyEdgeType edgeType, LinkedList<LinkedList<String>> attributes) {
    if (edge == null) {
      this.edgeSource.setText("");
      this.edgeDestination.setText("");
    }
    else {
      this.edgeSource.setText(edge.getSourceNode().getRealName());
      this.edgeDestination.setText(edge.getDestinationNode().getRealName());
    }

    if (edgeType == null) {
      this.edgetype.setText("");
    }
    else {
      this.edgetype.setText(edgeType.getName());
    }
    if (attributes == null) {
      for (int i = 0; i < edgeAttributesModel.getRowCount(); i++) {
        edgeAttributesModel.removeRow(i);
      }
      edgeAttributesModel.setRowCount(0);
    }
    else {
      for (int i = 0; i < edgeAttributesModel.getRowCount(); i++) {
        edgeAttributesModel.removeRow(i);
      }
      edgeAttributesModel.setRowCount(0);

      for (LinkedList<String> currentAttr : attributes) {
        String name = currentAttr.get(0);
        String value = currentAttr.get(1);
        String unit = currentAttr.get(2);
        String type = currentAttr.get(3);
        edgeAttributesModel.addRow(new String[] { name, value, unit, type });
      }
    }

    if (graph.isCollapsable()) {
      collapseExpand.setText("Collapse Node");
    }

    if (graph.isExpandable()) {
      collapseExpand.setText("Expand Node");
    }

    if (!graph.isCollapsable() && !graph.isExpandable()) {
      collapseExpand.setText("Collapse/Expand Node");
      collapseExpand.setEnabled(false);
    }
    else {
      collapseExpand.setEnabled(true);
    }

  }

  private void addNodeChangeListener() {
    PickedState<MyNode> pickedState = graph.getVisualisationViewer().getPickedVertexState();

    if (edgeListener != null) {
      pickedState.removeItemListener(edgeListener);
    }

    nodeListener = new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        @SuppressWarnings("unchecked")
        PickedState<MyNode> pi = (PickedState<MyNode>) e.getSource();
        Object subject = e.getItem();

        if (subject instanceof MyNode) {
          MyNode vertex = (MyNode) subject;
          if (pi.isPicked(vertex)) {
            updateNodeSidebar(vertex.getName(), vertex.getNodeType(), vertex.getAttributes());
          }
          else {
            updateNodeSidebar(null, null, null);
          }
        }
        else {
          updateNodeSidebar(null, null, null);
        }

      }
    };

    pickedState.addItemListener(nodeListener);

    Set<MyNode> s = pickedState.getPicked();
    if (s == null) {
      updateNodeSidebar(null, null, null);
    }
  }

  private void addEdgeChangeListener() {
    PickedState<MyEdge> pickedState = graph.getVisualisationViewer().getPickedEdgeState();
    if (nodeListener != null) {
      pickedState.removeItemListener(nodeListener);
    }

    edgeListener = new ItemListener() {

      @Override
      public void itemStateChanged(ItemEvent e) {
        @SuppressWarnings("unchecked")
        PickedState<MyEdge> pi = (PickedState<MyEdge>) e.getSource();
        Object subject = e.getItem();

        if (subject instanceof MyEdge) {
          MyEdge edge = (MyEdge) subject;
          if (pi.isPicked(edge)) {
            updateEdgeSidebar(edge, edge.getEdgeType(), edge.getAttributesForTable());
          }
          else {
            updateEdgeSidebar(null, null, null);
          }
        }
        else {
          updateEdgeSidebar(null, null, null);
        }

      }
    };
    pickedState.addItemListener(edgeListener);
    Set<MyEdge> s = pickedState.getPicked();
    if (s == null) {
      updateEdgeSidebar(null, null, null);
    }
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public void setDepthSpinnerValue(int value) {
    if (value > 0 && value < 101 && depthSpinner != null) {
      depthSpinner.getModel().setValue(value);
    }
  }

  public int getDepthSpinnerValue() {
    //		if (depthSpinner!=null)
    //		{
    //			return (int) depthSpinner.getModel().getValue();
    //		}
    //		else
    return 1;
  }

}
