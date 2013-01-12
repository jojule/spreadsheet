package org.vaadin.spreadsheet.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.spreadsheet.Spreadsheet;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;

public class SpreadsheetUploadDemo extends UI implements Receiver
{
	Upload upload = new Upload("Upload a XLS file to view it", this);
	Spreadsheet spreadsheet = new Spreadsheet();
	
    protected void init(VaadinRequest request) {
    	getPage().setTitle("Spreadsheet demo");
    	VerticalLayout vl = new VerticalLayout();
    	vl.setMargin(true);
    	vl.setSizeFull();
    	vl.addComponent(upload);
    	setContent(vl);
		vl.addComponent(spreadsheet);
		vl.setExpandRatio(spreadsheet, 1.0f);
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
