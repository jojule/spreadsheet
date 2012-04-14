package org.vaadin.spreadsheet.gwt.client;

import java.util.HashMap;

import com.vaadin.terminal.gwt.client.ComponentState;

public class SpreadsheetState extends ComponentState implements
		SpreadsheetModel {

	// Key: <row>,<col>. For example "2,3"
	HashMap<String, String> cellContentsHtml = new HashMap<String, String>();
	int rows;
	int cols;

	
	public HashMap<String, String> getCellContentsHtml() {
		return cellContentsHtml;
	}

	public void setCellContentsHtml(HashMap<String, String> cellContentsHtml) {
		this.cellContentsHtml = cellContentsHtml;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getRowHeight(int row) {
		return 20;
	}

	public int getColWidth(int col) {
		return 100;
	}

	public String getColHeader(int col) {
		return "" + col;
	}

	public String getRowHeader(int row) {
		return "" + row;
	}

	public String getCellHtml(int row, int col) {
		String html = cellContentsHtml.get(row + "," + col);
		return html == null ? "" : html;
	}

	public void setCellHtml(int row, int col, String html) {
		cellContentsHtml.put(row + "," + col, html);
	}

	public String getCellStyle(int col, int row) {
		return "";
	}
	
	/** Remove all cells from the spreadsheet, but do not change dimensions of the sheet. */
	public void clearCells() {
		cellContentsHtml.clear();
	}

}
