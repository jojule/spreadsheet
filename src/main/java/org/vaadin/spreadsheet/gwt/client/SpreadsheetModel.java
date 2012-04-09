package org.vaadin.spreadsheet.gwt.client;

public interface SpreadsheetModel {
	int getRows();
	int getCols();
	int getRowHeight(int row);
	int getColWidth(int col);
	String getColHeader(int col);
	String getRowHeader(int row);
	String getCell(int col, int row);
	String getCellStyle(int col, int row);
}
