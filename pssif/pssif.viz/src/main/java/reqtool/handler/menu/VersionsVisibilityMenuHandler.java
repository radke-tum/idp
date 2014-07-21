package reqtool.handler.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import reqtool.RequirementVersionManager;
import reqtool.event.HideVersionsEvent;
import reqtool.event.ShowVersionsEvent;
import reqtool.event.bus.ReqToolReqistry;
import reqtool.event.menu.VersionVisibilityMenuEvent;

import com.google.common.eventbus.Subscribe;

public class VersionsVisibilityMenuHandler implements MenuEventHandler {

	@Subscribe
	public void handleEventMenu(final VersionVisibilityMenuEvent event) {
		JMenuItem submenu;
    	
    	if (RequirementVersionManager.getMinVersion(event.getSelectedNode()).isVisible()
    			&& !RequirementVersionManager.getMinVersion(event.getSelectedNode()).equals(RequirementVersionManager.getMaxVersion(event.getSelectedNode()))
    			){
    	
    		submenu = new JMenuItem("Hide Versions");
        	submenu.addActionListener(new ActionListener() {
    			
        		@Override
            	public void actionPerformed(ActionEvent e) {
        			ReqToolReqistry.getInstance().post(new HideVersionsEvent(event.getSelectedNode(), event.getGViz()));
            	}
    		}
        	);
    	} else {
    		submenu = new JMenuItem("Show Versions");
        	submenu.addActionListener(new ActionListener() {
    			
        		@Override
            	public void actionPerformed(ActionEvent e){
        			ReqToolReqistry.getInstance().post(new ShowVersionsEvent(event.getSelectedNode(), event.getGViz()));
            	}
    		}
        	);
    		
    	}
    	event.getMenu().add(submenu);
	}
	
}
