package org.vaadin.spreadsheet.gwt.client;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;

public class SpreadsheetView extends Widget {

	/**
	 * Fixed column header height, including borders. Keep in sync with style
	 * sheet.
	 */
	private static int COLUMN_HEADER_HEIGHT = 19;

	/** Fixed row header width, including borders. Keep in sync with stylesheet. */
	private static int ROW_HEADER_WIDTH = 50;

	/** Dynamic graph height in pixels */
	private static int GRAPH_HEIGHT = 100;

	/** Dynamic graph width in pixels */
	private static int GRAPH_WIDTH = 200;

	/** Should we draw graph while mouse is over a column */
	private boolean graphEnabled = false;

	/**
	 * Column number (1..) for the column graph is currently shown, or 0 if not
	 * shown
	 */
	private int graphColumn;

	/** Canvas for drawing the hovering graph */
	private CanvasElement graph = Document.get().createCanvasElement();

	/** Drawing context for modifying graph */
	private Context2d graphCx = graph.getContext2d();

	/** Spreadsheet main (outmost) element */
	private DivElement spreadsheet = Document.get().createDivElement();

	/** Sheet that will contain all the cells */
	private DivElement sheet = Document.get().createDivElement();

	/** Header corner element that covers crossing headers */
	private DivElement corner = Document.get().createDivElement();

	/**
	 * Div elements for row header divs. Note that index 0 in array points to
	 * div on row 1
	 */
	private ArrayList<DivElement> rowHeaders = new ArrayList<DivElement>();

	/**
	 * Div elements for column header divs. Note that index 0 in array points to
	 * div on column 1
	 */
	private ArrayList<DivElement> colHeaders = new ArrayList<DivElement>();

	/**
	 * List of rows. Each row is a list of divs on that row. Note that index 0
	 * in the outer list points to row 1 and index 0 in the inner list points to
	 * div in column 1
	 */
	private ArrayList<ArrayList<DivElement>> rows = new ArrayList<ArrayList<DivElement>>();

	/** Stylesheet element created for holding the dynamic row and column styles */
	private StyleElement style = Document.get().createStyleElement();

	/** Model that stores the contents of the spreadsheet */
	private SpreadsheetModel model;

	/**
	 * Random id used as additional style for the widget element to connect
	 * dynamic CSS rules to correct spreadsheet.
	 */
	private String sheetId;

	/**
	 * Constructor just initializes DOM and listeners. You must call setModel()
	 * to show something on the page.
	 */
	public SpreadsheetView() {
		initDOM();
		initListeners();
	}

	/** Get the model that stores the contents of the spreadsheet. */
	public SpreadsheetModel getModel() {
		return model;
	}

	/**
	 * Set the model that stores the contents of the spreadsheet. Setting model
	 * redraws the sheet.
	 */
	public void setModel(SpreadsheetModel model) {
		this.model = model;
		graphColumn = 0;
		updateStyles();
		updateCells();
		updateHeaders();
	}

	/** Initialize scroll and mouse listeners to make spreadsheet interactive */
	private void initListeners() {
		Event.sinkEvents(sheet, Event.ONSCROLL | Event.ONMOUSEMOVE
				| Event.ONMOUSEOUT);
		Event.setEventListener(sheet, new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONSCROLL) {
					moveHeadersToMatchScroll();
				} else if (event.getTypeInt() == Event.ONMOUSEMOVE
						&& isGraphEnabled()) {
					updateGraphAfterMouseMove(event);
				} else if (event.getTypeInt() == Event.ONMOUSEOUT) {
					hideGraph();
				}
			}
		});
	}

	/** Called after scrolling to move headers in order to keep them in sync with the spreadsheet contents */
	private void moveHeadersToMatchScroll() {
		updateCSSRule(style, ".v-spreadsheet .ch", "marginLeft",
				(50 - sheet.getScrollLeft()) + "px");
		updateCSSRule(style, ".v-spreadsheet .rh", "marginTop",
				(19 - sheet.getScrollTop()) + "px");
	}

	/** Draw floating graph to visualize given data. */
	private void redrawGraph(double[] data) {
		
		// Clear background
		graphCx.setFillStyle("#00b4f0");
		graphCx.fillRect(0, 0, GRAPH_WIDTH, GRAPH_HEIGHT);
		if (data.length < 2)
			return;
		
		// Find out the range of the data
		double min = data[0];
		double max = data[0];
		for (int i = 1; i < data.length; i++) {
			if (data[i] > max)
				max = data[i];
			if (data[i] < min)
				min = data[i];
		}
		if (max == min) {
			max += 0.5;
			min -= 0.5;
		}
		
		// Draw linegraph 
		graphCx.setStrokeStyle("#aff");
		graphCx.setLineWidth(10);
		graphCx.beginPath();
		for (int i = 0; i < data.length; i++) {
			int x = 2 + (GRAPH_WIDTH - 4) * i / (data.length - 1);
			int y = (int) ((GRAPH_HEIGHT - 4) * (1.0 - (data[i] - min)
					/ (max - min))) + 2;
			if (i == 0)
				graphCx.moveTo(0, y);
			else
				graphCx.lineTo(x, y);
		}
		graphCx.stroke();
	}

	/** Build DOM elements for this spreadsheet */
	private void initDOM() {

		// Spreadsheet main element that acts as a viewport containing all the
		// other parts
		setElement(spreadsheet);
		spreadsheet.appendChild(sheet);
		sheetId = "spreadsheet-" + ((int) (Math.random() * 100000));
		spreadsheet.addClassName("v-spreadsheet");
		// TODO add when #8664 is fixed: spreadsheet.addClassName(sheetId);

		// Sheet where cells are stored
		sheet.setClassName("sheet");

		// Dynamic styles for this spreadsheet
		style.setType("text/css");
		style.setId(sheetId + "-style");
		Document.get().getBody().getParentElement().getFirstChild()
				.appendChild(style);

		// Corner div
		corner.setClassName("corner");
		spreadsheet.appendChild(corner);

		// Canvas
		spreadsheet.appendChild(graph);
		graph.getStyle().setVisibility(Visibility.HIDDEN);
		graph.getStyle().setWidth(GRAPH_WIDTH, Unit.PX);
		graph.getStyle().setHeight(GRAPH_HEIGHT, Unit.PX);
	}

	/** Remove extra DOM elements created */
	private void cleanDOM() {
		// TODO remove stylesheet
		// TODO call this from correct place
	}

	/** Update styles in for this spreadsheet */
	private void updateStyles() {
		String[] rules = new String[model.getRows() + model.getCols() + 2];
		int ruleIndex = 0;
		int height = 0;
		for (int i = 1; i <= model.getRows(); i++) {
			rules[ruleIndex++] = "." + getStylePrimaryName() + " .row" + i
					+ " { height: " + (model.getRowHeight(i) - 1) + "px; top: "
					+ height + "px;}\n";
			height += model.getRowHeight(i);
		}
		int width = 0;
		for (int i = 1; i <= model.getCols(); i++) {
			rules[ruleIndex++] = "." + getStylePrimaryName() + " .col" + i
					+ " { width: " + (model.getColWidth(i) - 1) + "px; left: "
					+ width + "px;}\n";
			width += model.getColWidth(i);
		}
		rules[ruleIndex++] = "." + getStylePrimaryName()
				+ " .rh { margin-top: 19px; }";
		rules[ruleIndex++] = "." + getStylePrimaryName()
				+ " .ch { margin-left: 50px; }";

		resetStyleSheetRules(style, rules);
	}

	/** Replace stylesheet with the array of rules given */
	static private void resetStyleSheetRules(StyleElement stylesheet,
			String[] rules) {
		// TODO remove all rules
		for (int i = 0; i < rules.length; i++) {
			insertRule(stylesheet, rules[i]);
		}
	}

	/** Insert one CSS rule to given stylesheet */
	public final static native void insertRule(StyleElement stylesheet,
			String css)
	/*-{
		 var isIE = navigator&&navigator.userAgent&&navigator.userAgent.match(/\bMSIE ([678])\./);
		 if (isIE)
		 	stylesheet.sheet.cssText += css;
		 else
		 	stylesheet.sheet.insertRule(css, stylesheet.sheet.cssRules.length);
	}-*/;

	/** Search and update a given CSS rule in a stylesheet */
	public final static native void updateCSSRule(StyleElement stylesheet,
			String selector, String property, String value)
	/*-{
		var classes = stylesheet.sheet.rules || stylesheet.sheet.cssRules
		for(var x=0;x<classes.length;x++) {
			if(classes[x].selectorText==selector) {
				classes[x].style[property]=value;
			}
		}	
	}-*/;

	/** Update the headers to match the model. Create and recycle header divs as needed. */
	private void updateHeaders() {
		rowHeaders.ensureCapacity(model.getRows());
		colHeaders.ensureCapacity(model.getCols());
		for (int i = 0; i < model.getCols(); i++) {
			DivElement colHeader;
			if (i < colHeaders.size())
				colHeader = colHeaders.get(i);
			else {
				colHeader = Document.get().createDivElement();
				spreadsheet.insertBefore(colHeader, corner);
				colHeaders.add(i, colHeader);
				colHeader.setClassName("ch col" + (i + 1));
			}
			colHeader.setInnerHTML(model.getColHeader(i + 1));
		}
		for (int i = 0; i < model.getRows(); i++) {
			DivElement rowHeader;
			if (i < rowHeaders.size())
				rowHeader = rowHeaders.get(i);
			else {
				rowHeader = Document.get().createDivElement();
				spreadsheet.insertBefore(rowHeader, corner);
				rowHeaders.add(i, rowHeader);
				rowHeader.setClassName("rh row" + (i + 1));
			}
			rowHeader.setInnerHTML(model.getRowHeader(i + 1));
		}
		// Remove unused headers
		while (rowHeaders.size()>model.getRows()) {
			spreadsheet.removeChild(rowHeaders.get(model.getRows()));
			rowHeaders.remove(model.getRows());
		}
		while (colHeaders.size()>model.getCols()) {
			spreadsheet.removeChild(colHeaders.get(model.getCols()));
			colHeaders.remove(model.getCols());
		}
	}

	/** Update the data cells in the spreadsheet to reflect the current model */ 
	private void updateCells() {
		// Remove old cells
		for (ArrayList<DivElement> row : rows) {
			for (DivElement cell : row) {
				sheet.removeChild(cell);
			}
			row.clear();
		}
		rows.clear();
		
		// Add new cells
		rows.ensureCapacity(model.getRows());
		for (int i = 0; i < model.getRows(); i++) {
			ArrayList<DivElement> row = new ArrayList<DivElement>();
			row.ensureCapacity(model.getCols());
			rows.add(row);
			for (int j = 0; j < model.getCols(); j++) {
				DivElement cell = Document.get().createDivElement();
				sheet.appendChild(cell);
				row.add(cell);
				cell.setClassName("col" + (j + 1) + " row" + (i + 1));
				cell.setInnerHTML(model.getCellHtml(i + 1, j + 1));
				cell.setAttribute("style", model.getCellStyle(j + 1, i + 1));
			}
		}
	}

	/** Is the floating graph drawn */
	public boolean isGraphEnabled() {
		return graphEnabled;
	}

	/** Set if the floating graph is drawn */
	public void setGraphEnabled(boolean graphEnabled) {
		this.graphEnabled = graphEnabled;
		if (!graphEnabled) {
			hideGraph();
		}

	}

	/** Update the floating graph position and contents after each mouse move event. */
	private void updateGraphAfterMouseMove(Event event) {
		graphColumn = 0;
		graph.getStyle().setVisibility(Visibility.VISIBLE);
		graph.getStyle().setTop(
				event.getClientY() - getElement().getAbsoluteTop()
						- GRAPH_HEIGHT / 2 + COLUMN_HEADER_HEIGHT, Unit.PX);
		graph.getStyle().setLeft(
				event.getClientX() - getElement().getAbsoluteLeft() + 20,
				Unit.PX);
		int newColumn = 1;
		int w = event.getClientX() - getElement().getAbsoluteLeft()
				- ROW_HEADER_WIDTH + sheet.getScrollLeft();
		while (w > model.getColWidth(newColumn)) {
			w -= model.getColWidth(newColumn);
			newColumn++;
		}
		if (newColumn != graphColumn) {
			graphColumn = newColumn;
			LinkedList<Double> ll = new LinkedList<Double>();
			for (int i = 0; i < model.getRows(); i++) {
				String value = model.getCellHtml(i + 1, newColumn);
				try {
					double nv = Double.parseDouble(value);
					ll.add(new Double(nv));
				} catch (NumberFormatException ignored) {
				}
			}
			if (ll.size() > 0) {
				double data[] = new double[ll.size()];
				for (int i = 0; i < data.length; i++)
					data[i] = ll.get(i).doubleValue();
				redrawGraph(data);
			} else
				graph.getStyle().setVisibility(Visibility.HIDDEN);
		}
	}

	/** Hide the graph element */
	private void hideGraph() {
		graphColumn = 0;
		graph.getStyle().setVisibility(Visibility.HIDDEN);
	}
}
