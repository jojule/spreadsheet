package org.vaadin.spreadsheet.gwt.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.ui.Widget;

public class SpreadsheetView extends Widget {

	DivElement spreadsheet = Document.get().createDivElement();
	DivElement sheet = Document.get().createDivElement();
	ArrayList<DivElement> rowHeaders = new ArrayList<DivElement>();
	ArrayList<DivElement> colHeaders = new ArrayList<DivElement>();
	ArrayList<ArrayList<DivElement>> rows = new ArrayList<ArrayList<DivElement>>();
	StyleElement style = Document.get().createStyleElement();
	SpreadsheetModel model;
	String sheetId;

	public SpreadsheetView(SpreadsheetModel model) {
		this.model = model;
		initDOM();
		updateSheetSize();
		createCells();
		updateHeaders();
	}

	private void initDOM() {
		setElement(spreadsheet);
		spreadsheet.getStyle().setOverflow(Overflow.AUTO);
		spreadsheet.appendChild(sheet);
		spreadsheet.setClassName("v-spreadsheet");
		sheetId = "sheet-" + ((int) (Math.random() * 100000));
		sheet.setId(sheetId);
		style.setType("text/css");
	}

	private void updateSheetSize() {
		StringBuffer cssText = new StringBuffer();
		int height = 0;
		for (int i = model.getRows(); i >= 0; i--) {
			height += model.getRowHeight(i);
			cssText.append("#" + sheetId + " .row" + i + " { height: "
					+ model.getRowHeight(i) + "px;}\n");
		}
		sheet.getStyle().setHeight(height, Unit.PX);
		int width = 0;
		for (int i = model.getCols(); i >= 0; i--) {
			width += model.getColWidth(i);
			cssText.append("#" + sheetId + " .col" + i + " { width: "
					+ model.getRowHeight(i) + "px;}\n");
		}
		sheet.getStyle().setWidth(width, Unit.PX);
		style.setCssText(cssText.toString());
	}

	private void updateHeaders() {
		rowHeaders.ensureCapacity(model.getRows());
		colHeaders.ensureCapacity(model.getCols());
		for (int i = 0; i<model.getCols(); i++) {
			DivElement colHeader;
			if (i<colHeaders.size())
				colHeader = colHeaders.get(i);
			else {
				colHeader = Document.get().createDivElement();
				sheet.appendChild(colHeader);
				colHeaders.add(i, colHeader);
				colHeader.setClassName("ch row" + i);
			}
			colHeader.setInnerHTML(model.getColHeader(i));
		}
		for (int i = 0; i<model.getRows(); i++) {
			DivElement rowHeader;
			if (i<rowHeaders.size())
				rowHeader = rowHeaders.get(i);
			else {
				rowHeader = Document.get().createDivElement();
				sheet.appendChild(rowHeader);
				rowHeaders.add(i, rowHeader);
				rowHeader.setClassName("rh row" + i);
			}
			rowHeader.setInnerHTML(model.getRowHeader(i));
		}
	}

	// TODO rewrite this test code
	private void createCells() {
		rows.ensureCapacity(model.getRows());
		for (int i = 0; i< model.getRows(); i++) {
			ArrayList<DivElement> row = new ArrayList<DivElement>();
			row.ensureCapacity(model.getCols());
			rows.add(row);
			for (int j = 0; j < model.getCols();j++) {
				DivElement cell = Document.get().createDivElement();
				sheet.appendChild(cell);
				row.add(cell);
				cell.setClassName("col" + j + " row" + i);
				cell.setInnerHTML(model.getCell(j, i));
				cell.setAttribute("style", model.getCellStyle(j, i));
			}
		}
	}
}
