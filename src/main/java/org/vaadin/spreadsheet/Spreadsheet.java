package org.vaadin.spreadsheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.vaadin.spreadsheet.gwt.client.SpreadsheetState;

import com.vaadin.ui.AbstractComponent;

public class Spreadsheet extends AbstractComponent {

	public Spreadsheet() {
		setSizeFull(); // Default to full size
	}

	@Override
	public SpreadsheetState getState() {
		return (SpreadsheetState) super.getState();
	}

	public int getCols(int cols) {
		return getState().getCols();
	}

	public int getRows() {
		return getState().getRows();
	}

	public void setCols(int cols) {
		getState().setCols(cols);
		requestRepaint();
	}

	public void setRows(int rows) {
		getState().setRows(rows);
		requestRepaint();
	}

	/**
	 * Get contents of a sell as HTML. Columns are indexed from 1 to
	 * getColumns(). Rows are indexed from 1 to getRows().
	 */
	String getCellHtml(int row, int col) {
		return getState().getCellHtml(col, row);
	}

	/**
	 * Set contents of a sell as HTML. Columns are indexed from 1 to
	 * getColumns(). Rows are indexed from 1 to getRows().
	 */
	void setCellHtml(int row, int col, String contents) {
		getState().setCellHtml(col, row, contents);
		requestRepaint();
	}

	void readXLS(File file) throws FileNotFoundException, IOException {

		// Load XLS file
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook workbook = new HSSFWorkbook(fs);
		HSSFSheet sheet = workbook.getSheetAt(0);
		FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();
		
		// Load cells from the file
		getState().clearCells();
		setRows(sheet.getLastRowNum() + 1);
		int cols = 0;
		Iterator<?> ri = sheet.rowIterator();
		while (ri.hasNext()) {
			HSSFRow row = (HSSFRow) ri.next();
			Iterator<?> ci = row.cellIterator();
			while (ci.hasNext()) {
				HSSFCell cell = (HSSFCell) ci.next();
				String contents = cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA ? ""
						+ evaluator.evaluate(cell).getNumberValue()
						: cell.toString();
				int c = cell.getColumnIndex() + 1;
				if (c > cols)
					cols = c;
				setCellHtml(row.getRowNum() + 1, c, contents);
			}
		}
		setCols(cols);
	}

}
