package org.vaadin.spreadsheet.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.spreadsheet.Spreadsheet;

import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Root;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;

public class SpreadsheetUploadDemo extends Root implements Receiver
{
	Upload upload = new Upload("Upload a XLS file to view it", this);
	Spreadsheet spreadsheet = new Spreadsheet();
	
    protected void init(WrappedRequest request) {
    	setCaption("Spreadsheet component XLS upload demo");
    	VerticalLayout vl = new VerticalLayout();
    	vl.setMargin(true);
    	vl.setSizeFull();
    	vl.addComponent(upload);
    	setContent(vl);
		vl.addComponent(spreadsheet);
		vl.setExpandRatio(spreadsheet, 1.0f);
//		try {
//			spreadsheet.readXLS(getClass().getResourceAsStream("test.xls"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		spreadsheet.setGraphEnabled(true);
	}

	public OutputStream receiveUpload(final String filename, String mimeType) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream() {
			public void close() throws IOException {
				super.close();
				spreadsheet.setCaption(filename);
				spreadsheet.readXLS(new ByteArrayInputStream(this.toByteArray()));
			}
		};
		return baos;
	}
    
}
