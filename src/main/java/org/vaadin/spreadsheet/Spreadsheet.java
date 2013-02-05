package org.vaadin.spreadsheet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

	/** Get the number of columns in the spreadsheet */
	public int getCols(int cols) {
		return getState().getCols();
	}

	/** Get the number of rows in the spreadsheet */
	public int getRows() {
		return getState().getRows();
	}

	/**
	 * Set the number of columns in the spreadsheet. Any unset cells are left
	 * empty. Any cells outside the given columns are hidden.
	 */
	public void setCols(int cols) {
		getState().setCols(cols);
	}

	/**
	 * Set the number of rows in the spreadsheet. Any unset cells are left
	 * empty. Any cells outside the given rows are hidden.
	 */
	public void setRows(int rows) {
		getState().setRows(rows);
	}

	/**
	 * Get contents of a sell as HTML. Columns are indexed from 1 to
	 * getColumns(). Rows are indexed from 1 to getRows().
	 */
	public String getCellHtml(int row, int col) {
		return getState().getCellHtml(row, col);
	}

	/**
	 * Set contents of a sell as HTML. Columns are indexed from 1 to
	 * getColumns(). Rows are indexed from 1 to getRows().
	 */
	public void setCellHtml(int row, int col, String contents) {
		getState().setCellHtml(row, col, contents);
	}

	/**
	 * Reset the spreadsheet contents with an excel file read from the given
	 * InputStream.
	 */
	public void readXLS(InputStream file) throws FileNotFoundException,
			IOException {

		// Load XLS file
		POIFSFileSystem fs = new POIFSFileSystem(file);
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

	/** Is the floating graph drawn */
	public boolean isGraphEnabled() {
		return getState().isGraphEnabled();
	}

	/** Set if the floating graph is drawn */
	public void setGraphEnabled(boolean graphEnabled) {
		getState().setGraphEnabled(graphEnabled);
	}
}
