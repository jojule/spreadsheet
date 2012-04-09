package org.vaadin.spreadsheet.gwt.client;

import org.vaadin.spreadsheet.gwt.client.test.SpreadsheetModelMockup;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class SpreadsheetWidget extends SpreadsheetView implements Paintable {

    protected String paintableId;
    ApplicationConnection client;

    public SpreadsheetWidget() {
    	super(new SpreadsheetModelMockup());
    }
    
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) return;
        this.client = client;
        paintableId = uidl.getId();
    }

}
