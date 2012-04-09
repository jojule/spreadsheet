package org.vaadin.spreadsheet.gwt.client.test;

import org.vaadin.spreadsheet.gwt.client.SpreadsheetModel;

public class SpreadsheetModelMockup implements SpreadsheetModel {

	public int getRows() {
		return 300;
	}

	public int getCols() {
		return 15;
	}

	public int getRowHeight(int row) {
		return 20;
	}

	public int getColWidth(int col) {
		return 150;
	}

	public String getColHeader(int col) {
		return "Col " + col;
	}

	public String getRowHeader(int row) {
		return "Row " + row;
	}

	public String getCell(int col, int row) {
		return "" + col + "," + row;
	}

	public String getCellStyle(int col, int row) {
		return "";
	}

}
