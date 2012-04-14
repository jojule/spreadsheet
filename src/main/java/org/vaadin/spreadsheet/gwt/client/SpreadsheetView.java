package org.vaadin.spreadsheet.gwt.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class SpreadsheetView extends Widget {

	private static int COLUMN_HEADER_HEIGHT=19;
	private static int ROW_HEADER_WIDTH=50;
	
	DivElement spreadsheet = Document.get().createDivElement();
	DivElement sheet = Document.get().createDivElement();
	ArrayList<DivElement> rowHeaders = new ArrayList<DivElement>();
	ArrayList<DivElement> colHeaders = new ArrayList<DivElement>();
	ArrayList<ArrayList<DivElement>> rows = new ArrayList<ArrayList<DivElement>>();
	StyleElement style = Document.get().createStyleElement();
	SpreadsheetModel model;
	
	public SpreadsheetModel getModel() {
		return model;
	}

	public void setModel(SpreadsheetModel model) {
		this.model = model;
		updateStyles();
		updateCells();
		updateHeaders();
	}

	String sheetId;

	public SpreadsheetView() {
		initDOM();
	}

	/** Build DOM elements for this spreadsheet */
	private void initDOM() {
		
		// Spreadsheet main element that acts as a viewport containing all the other parts
		setElement(spreadsheet);
		spreadsheet.getStyle().setOverflow(Overflow.AUTO);
		spreadsheet.appendChild(sheet);
		spreadsheet.setClassName("v-spreadsheet");
		
		// Sheet where cells are stored
		sheetId = "sheet-" + ((int) (Math.random() * 100000));
		sheet.setId(sheetId);
		sheet.setClassName("sheet");

		// Dynamic styles for this spreadsheet
		style.setType("text/css");
		style.setId(sheetId + "-style");
		Document.get().getBody().getParentElement().getFirstChild()
				.appendChild(style);
	}
	
	/** Remove extra DOM elements created */
	private void cleanDOM() {
		// TODO remove stylesheet
		// TODO call this from correct place
	}

	/** Update styles in for this spreadsheet */
	private void updateStyles() {
		String[] rules = new String[model.getRows() + model.getCols()];
		int ruleIndex = 0;
		int height = COLUMN_HEADER_HEIGHT; 
		for (int i = 1; i<= model.getRows(); i++) {
			rules[ruleIndex++] = "#" + sheetId + " .row" + i + " { height: "
					+ (model.getRowHeight(i)-1) + "px; top: " + height + "px;}\n";
			height += model.getRowHeight(i);
		}
		sheet.getStyle().setHeight(height, Unit.PX);
		int width = ROW_HEADER_WIDTH; 
		for (int i = 1; i<= model.getCols(); i++) {
			rules[ruleIndex++] = "#" + sheetId + " .col" + i + " { width: "
					+ (model.getColWidth(i)-1) + "px; left: " + width + "px;}\n";
			width += model.getColWidth(i);
		}
		sheet.getStyle().setWidth(width, Unit.PX);
		resetStyleSheetRules(style, rules);
	}

	static private void resetStyleSheetRules(StyleElement stylesheet,
			String[] rules) {
		// TODO remove all rules
		for (int i = 0; i < rules.length; i++) {
			insertRule(stylesheet, rules[i]);
		}
	}

	public final static native void insertRule(StyleElement stylesheet, String css) /*-{
		// TODO For IE, just use setCssText that happens to work on GWT
		stylesheet.sheet.insertRule(css, stylesheet.sheet.cssRules.length);
	}-*/;

	
	private void updateHeaders() {
		rowHeaders.ensureCapacity(model.getRows());
		colHeaders.ensureCapacity(model.getCols());
		for (int i = 0; i < model.getCols(); i++) {
			DivElement colHeader;
			if (i < colHeaders.size())
				colHeader = colHeaders.get(i);
			else {
				colHeader = Document.get().createDivElement();
				sheet.appendChild(colHeader);
				colHeaders.add(i, colHeader);
				colHeader.setClassName("ch row" + (i+1));
			}
			colHeader.setInnerHTML(model.getColHeader(i+1));
		}
		for (int i = 0; i < model.getRows(); i++) {
			DivElement rowHeader;
			if (i < rowHeaders.size())
				rowHeader = rowHeaders.get(i);
			else {
				rowHeader = Document.get().createDivElement();
				sheet.appendChild(rowHeader);
				rowHeaders.add(i, rowHeader);
				rowHeader.setClassName("rh row" + (i+1));
			}
			rowHeader.setInnerHTML(model.getRowHeader(i+1));
		}
	}

	// TODO rewrite this test code
	private void updateCells() {
		rows.ensureCapacity(model.getRows());
		for (int i = 0; i < model.getRows(); i++) {
			ArrayList<DivElement> row = new ArrayList<DivElement>();
			row.ensureCapacity(model.getCols());
			rows.add(row);
			for (int j = 0; j < model.getCols(); j++) {
				DivElement cell = Document.get().createDivElement();
				sheet.appendChild(cell);
				row.add(cell);
				cell.setClassName("col" + (j+1) + " row" + (i+1));
				cell.setInnerHTML(model.getCellHtml(j+1, i+1));
				cell.setAttribute("style", model.getCellStyle(j+1, i+1));
			}
		}
	}
}
