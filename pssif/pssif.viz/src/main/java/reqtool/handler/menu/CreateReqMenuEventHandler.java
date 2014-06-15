package reqtool.handler.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import model.ModelBuilder;
import reqtool.RequirementToolbox;
import reqtool.event.CreateTestCaseEvent;
import reqtool.event.HideContainementEvent;
import reqtool.event.NewDeriveRequirementEvent;
import reqtool.event.NewVersionEvent;
import reqtool.event.ResolveIssueEvent;
import reqtool.event.ShowContainementEvent;
import reqtool.event.VerifiTestCaseEvent;
import reqtool.event.bus.ReqToolReqistry;
import reqtool.event.menu.CreateReqMenuEvent;
import reqtool.event.menu.TraceReqMenuEvent;
import reqtool.event.menu.VersionVisibilityMenuEvent;
import reqtool.handler.EventHandler;

import com.google.common.eventbus.Subscribe;

import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;

public class CreateReqMenuEventHandler implements EventHandler {

	@Subscribe
	public void handleReqPopupEvent(CreateReqMenuEvent event) {
		if (event.getSelectedNode().getNodeType().equals((ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT)))) {
			
			JMenu reqMenu = new JMenu("Requirement Tool");

			ReqToolReqistry.getInstance().post(new TraceReqMenuEvent(event.getSelectedNode(), reqMenu, event.getGViz()));;
			
			JMenuItem deriveReqItem = deriveRequirement(event);
			JMenuItem subItem2 = createTestCase(event);
			JMenuItem subItem3 = createNewVersion(event);
			reqMenu.add(deriveReqItem);
			reqMenu.add(subItem2);
			reqMenu.add(getVersMngItem(event));
			event.getPopup().add(reqMenu);
			
		} else if (event.getSelectedNode().getNodeType().equals((ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_TEST_CASE)))) {
			
			JMenuItem subItem = verifyTestCase(event);
			event.getPopup().add(subItem);
		
		} else if (event.getSelectedNode().getNodeType().equals((ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_ISSUE)))) {
			
			JMenuItem subItem = resolveIssue(event);
			event.getPopup().add(subItem);
		
		} else if (RequirementToolbox.getSpecArtifTypes().contains(event.getSelectedNode().getNodeType())){
				
			JMenuItem subItem = showHideContainer(event);
			event.getPopup().add(subItem);
		}
		
		if (ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT).getType().isAssignableFrom(event.getSelectedNode().getNodeType().getType())) {

			event.getPopup().add(getVersMngItem(event));

		}

	}
	
	private JMenu getVersMngItem(CreateReqMenuEvent event) {
		JMenu versMenu = new JMenu("Version Management");
		
		JMenuItem subItem = createNewVersion(event);
		versMenu.add(subItem);
		ReqToolReqistry.getInstance().post(new VersionVisibilityMenuEvent(event.getSelectedNode(), versMenu, event.getGViz()));
		
		return versMenu;
	}
	
	private JMenuItem createTestCase(final CreateReqMenuEvent event) {
    	JMenuItem submenu;
    	submenu = new JMenuItem("Create Test Case");
    	submenu.addActionListener(new ActionListener() {

    		@Override
        	public void actionPerformed(ActionEvent e) {
    			ReqToolReqistry.getInstance().post(new CreateTestCaseEvent(event.getSelectedNode(), event.getGViz()));
        	}
		});
 		return submenu;
 	}
	
	private JMenuItem resolveIssue(final CreateReqMenuEvent event)  {
    	JMenuItem submenu;
    	submenu = new JMenuItem("Resolve Issue");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			ReqToolReqistry.getInstance().post(new ResolveIssueEvent(event.getSelectedNode(), event.getGViz()));
    		}
		});
 		return submenu;
	}
	
	private JMenuItem createNewVersion(final CreateReqMenuEvent event) {
    	JMenuItem submenu;
    	submenu = new JMenuItem("Create new version");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			ReqToolReqistry.getInstance().post(new NewVersionEvent(event.getSelectedNode(), event.getGViz()));
            }

        });
 		return submenu;
 	}
	
	private JMenuItem deriveRequirement(final CreateReqMenuEvent event) {
    	JMenuItem submenu;
    	submenu = new JMenuItem("Derive Requirement");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			ReqToolReqistry.getInstance().post(new NewDeriveRequirementEvent(event.getSelectedNode(), event.getGViz()));
            }

        });
 		return submenu;
 	}
	
	private JMenuItem verifyTestCase(final CreateReqMenuEvent event) {
    	JMenuItem submenu;
    	submenu = new JMenuItem("Verify Test Case");
    	submenu.addActionListener(new ActionListener() {
    		@Override
        	public void actionPerformed(ActionEvent e) {
    			ReqToolReqistry.getInstance().post(new VerifiTestCaseEvent(event.getSelectedNode(), event.getGViz()));
        	}
		});
 		return submenu;
	}
	
	private JMenuItem showHideContainer(final CreateReqMenuEvent event) {
		JMenuItem submenu = new JMenuItem("");
		
		if (RequirementToolbox.hasContainment(event.getSelectedNode())) {
			if (RequirementToolbox.containmentIsVisible(event.getSelectedNode())) {

				submenu = new JMenuItem("Hide Containment");
				submenu.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						ReqToolReqistry.getInstance().post(new HideContainementEvent(event.getSelectedNode(), event.getGViz()));
					}
				});
			} else {
				submenu = new JMenuItem("Show Containment");
				submenu.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						ReqToolReqistry.getInstance().post(new ShowContainementEvent(event.getSelectedNode(), event.getGViz()));
					}
				});
			}
		} else {
			submenu = new JMenuItem("");
		}

		return submenu;
	}
	
}
