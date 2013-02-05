package org.vaadin.spreadsheet.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.spreadsheet.Spreadsheet;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;

@Title("Spreadsheet demo")
public class SpreadsheetUploadDemo extends UI implements Receiver {

	VerticalLayout layout = new VerticalLayout();

	Upload upload = new Upload("Upload a XLS file to view it", this);
	Spreadsheet spreadsheet = new Spreadsheet();

	protected void init(VaadinRequest request) {
		setContent(layout);
		
		layout.addComponent(spreadsheet);
		
		spreadsheet.setGraphEnabled(true);

		layout.setMargin(true);
		layout.setSizeFull();
		layout.addComponent(upload);
		layout.setExpandRatio(spreadsheet, 1.0f);
	}

	public OutputStream receiveUpload(final String filename, String mimeType) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream() {
			public void close() throws IOException {
				super.close();
				spreadsheet.setCaption(filename);
				spreadsheet
						.readXLS(new ByteArrayInputStream(this.toByteArray()));
			}
		};
		return baos;
	}
}
