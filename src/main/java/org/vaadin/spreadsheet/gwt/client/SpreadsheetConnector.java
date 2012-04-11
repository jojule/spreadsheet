package org.vaadin.spreadsheet.gwt.client;

import org.vaadin.spreadsheet.gwt.client.test.SpreadsheetModelMockup;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentConnector;

public class SpreadsheetConnector extends AbstractComponentConnector {
    protected Widget createWidget() {		
		return new SpreadsheetView(new SpreadsheetModelMockup());
	}

}
