package org.vaadin.spreadsheet.gwt.client;

import org.vaadin.spreadsheet.Spreadsheet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = Spreadsheet.class)
public class SpreadsheetConnector extends AbstractComponentConnector {

	protected Widget createWidget() {		
		return GWT.create(SpreadsheetView.class);
	}

	public SpreadsheetView getWidget() {
		return (SpreadsheetView) super.getWidget();
	}
	
	public SpreadsheetState getState() {
		return (SpreadsheetState) super.getState();
	}
	
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		getWidget().setModel(getState());
		getWidget().setGraphEnabled(getState().isGraphEnabled());
	}
}
