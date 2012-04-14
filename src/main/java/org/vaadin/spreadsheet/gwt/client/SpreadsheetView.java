package org.vaadin.spreadsheet.gwt.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;

public class SpreadsheetView extends Widget {

	private static int COLUMN_HEADER_HEIGHT = 19;
	private static int ROW_HEADER_WIDTH = 50;

	DivElement spreadsheet = Document.get().createDivElement();
	DivElement sheet = Document.get().createDivElement();
	DivElement corner = Document.get().createDivElement();
	ArrayList<DivElement> rowHeaders = new ArrayList<DivElement>();
	ArrayList<DivElement> colHeaders = new ArrayList<DivElement>();
	ArrayList<ArrayList<DivElement>> rows = new ArrayList<ArrayList<DivElement>>();
	StyleElement style = Document.get().createStyleElement();
	SpreadsheetModel model;
	String sheetId;

	public SpreadsheetModel getModel() {
		return model;
	}

	public void setModel(SpreadsheetModel model) {
		this.model = model;
		updateStyles();
		updateCells();
		updateHeaders();
	}

	public SpreadsheetView() {
		initDOM();
		initScrollListeners();
	}

	private void initScrollListeners() {
		Event.sinkEvents(sheet, Event.ONSCROLL);
		Event.setEventListener(sheet, new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONSCROLL) {
					updateCSSRule(style, ".v-spreadsheet .ch", "marginLeft",
							(50 - sheet.getScrollLeft()) + "px");
					updateCSSRule(style, ".v-spreadsheet .rh", "marginTop",
							(19 - sheet.getScrollTop()) + "px");
				}
			}
		});
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
	}

	/** Remove extra DOM elements created */
	private void cleanDOM() {
		// TODO remove stylesheet
		// TODO call this from correct place
	}

	/** Update styles in for this spreadsheet */
	private void updateStyles() {
		String[] rules = new String[model.getRows() + model.getCols()+2];
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
		rules[ruleIndex++] = "." + getStylePrimaryName() + " .rh { margin-top: 19px; }";
		rules[ruleIndex++] = "." + getStylePrimaryName() + " .ch { margin-left: 50px; }";

		resetStyleSheetRules(style, rules);
	}

	static private void resetStyleSheetRules(StyleElement stylesheet,
			String[] rules) {
		// TODO remove all rules
		for (int i = 0; i < rules.length; i++) {
			insertRule(stylesheet, rules[i]);
		}
	}

	public final static native void insertRule(StyleElement stylesheet,
			String css)
	/*-{
		// TODO For IE, just use setCssText that happens to work on GWT
		stylesheet.sheet.insertRule(css, stylesheet.sheet.cssRules.length);
	}-*/;

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

	private void updateHeaders() {
		rowHeaders.ensureCapacity(model.getRows());
		colHeaders.ensureCapacity(model.getCols());
		for (int i = 0; i < model.getCols(); i++) {
			DivElement colHeader;
			if (i < colHeaders.size())
				colHeader = colHeaders.get(i);
			else {
				colHeader = Document.get().createDivElement();
				getElement().insertBefore(colHeader, corner);
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
				getElement().insertBefore(rowHeader, corner);
				rowHeaders.add(i, rowHeader);
				rowHeader.setClassName("rh row" + (i + 1));
			}
			rowHeader.setInnerHTML(model.getRowHeader(i + 1));
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
				cell.setClassName("col" + (j + 1) + " row" + (i + 1));
				cell.setInnerHTML(model.getCellHtml(j + 1, i + 1));
				cell.setAttribute("style", model.getCellStyle(j + 1, i + 1));
			}
		}
	}
}
