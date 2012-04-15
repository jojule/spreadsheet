package org.vaadin.spreadsheet.gwt.client;

public interface SpreadsheetModel {
	/** Number of rows in the spreadsheet */
	int getRows();

	/** Number of columns in the spreadsheet */
	int getCols();

	/**
	 * Height of a row in pixels including bottom border. Rows are indexed from
	 * 1 to getRows().
	 */
	int getRowHeight(int row);

	/**
	 * Width of a row in pixels including right border. Columns are indexed from
	 * 1 to getColumns().
	 */
	int getColWidth(int col);

	/**
	 * Get header of a column as HTML. Columns are indexed from 1 to
	 * getColumns().
	 */
	String getColHeader(int col);

	/** Get header of a row as HTML. Rows are indexed from 1 to getRows(). */
	String getRowHeader(int row);

	/**
	 * Get contents of a sell as HTML. Columns are indexed from 1 to
	 * getColumns(). Rows are indexed from 1 to getRows().
	 */
	String getCellHtml(int row, int col);

	/**
	 * Set contents of a sell as HTML. Columns are indexed from 1 to
	 * getColumns(). Rows are indexed from 1 to getRows().
	 */
	String getCellStyle(int row, int col);	
}
