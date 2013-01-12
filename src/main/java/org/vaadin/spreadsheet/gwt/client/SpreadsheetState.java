package org.vaadin.spreadsheet.gwt.client;

import java.util.HashMap;

import com.vaadin.shared.AbstractComponentState;

public class SpreadsheetState extends AbstractComponentState implements
		SpreadsheetModel {

	/** Map from toKey() to cell contents as HTML */ 
	private HashMap<String, String> cellContentsHtml = new HashMap<String, String>();
	
	/** Number of rows in the spreadsheet */
	private int rows = 50;
	
	/** Number of columns in the spreadsheet */
	private int cols = 8;
	
	/** Is drawing of graph enable */
	private boolean graphEnabled = false;

	/** Map row and column to key used in cellContentsHtml map */
	private static String toKey(int row,int col) {
		return row + "," + col;
	}
	
	/** Is the floating graph drawn */
	public boolean isGraphEnabled() {
		return graphEnabled;
	}

	/** Set if the floating graph is drawn */
	public void setGraphEnabled(boolean graphEnabled) {
		this.graphEnabled = graphEnabled;
	}

	/** Get contents of all cells as toKey() -> HTML as String map */
	public HashMap<String, String> getCellContentsHtml() {
		return cellContentsHtml;
	}

	/** Set contents of all cells as toKey() -> HTML as String map */
	public void setCellContentsHtml(HashMap<String, String> cellContentsHtml) {
		this.cellContentsHtml = cellContentsHtml;
	}

	/** Set the number of rows in the spreadsheet */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/** Set the number of columns in the spreadsheet */
	public void setCols(int cols) {
		this.cols = cols;
	}

	/** Get the number of rows in the spreadsheet */
	public int getRows() {
		return rows;
	}

	/** Get the number of rows in the spreadsheet */
	public int getCols() {
		return cols;
	}

	/** Currently all rows are of the same height. */
public int getRowHeight(int row) {
		return 20;
	}

/** Currently all columns are of the same width. */
	public int getColWidth(int col) {
		return 200;
	}

	/** Get column header for column indexed 1.. */
	public String getColHeader(int col) {
		String h = "";
		while (col>0) {
			h=(char)('A' + (col-1)%26) + h;
			col=(col-1)/26;
		}
		return h;
	}

	/** Get row header for rows indexed 1.. */
	public String getRowHeader(int row) {
		return "" + row;
	}

	/** Get the contents of the cell as HTML. Rows and cols are indexed from 1. */
	public String getCellHtml(int row, int col) {
		String html = cellContentsHtml.get(toKey(row,col));
		return html == null ? "" : html;
	}

	/** Set the contents of the cell as HTML. Rows and cols are indexed from 1. */
	public void setCellHtml(int row, int col, String html) {
		cellContentsHtml.put(toKey(row,col), html);
	}

	/** Cell styles are not supported at the moment - just empty styles are returned. */
	public String getCellStyle(int row, int col) {
		return "";
	}
	
	/** Remove all cells from the spreadsheet, but do not change dimensions of the sheet. */
	public void clearCells() {
		cellContentsHtml.clear();
	}

}
