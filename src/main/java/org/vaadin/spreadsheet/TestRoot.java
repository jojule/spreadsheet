package org.vaadin.spreadsheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;

public class TestRoot extends Root
{
    protected void init(WrappedRequest request) {
    	VerticalLayout vl = new VerticalLayout();
    	vl.setSizeFull();
    	setContent(vl);
		Spreadsheet ss = new Spreadsheet();
		vl.addComponent(ss);
		vl.setExpandRatio(ss, 1.0f);
		
		try {
			ss.readXLS(new File("/Users/phoenix/Desktop/test.xls"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}
