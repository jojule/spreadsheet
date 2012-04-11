package org.vaadin.spreadsheet;

import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Root;

public class TestRoot extends Root
{
    protected void init(WrappedRequest request) {
		addComponent(new Spreadsheet());		
	}
    
}
