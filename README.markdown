# Spreadsheet for Vaadin

The widget shows a spreadsheet - either from XLS file or by setting the cell contents programmatically. 

This version is very limited and should be considered to be "pre-alpha" -version. Try out the demo to see if it would be useful for you. I mainly built it for an upcoming presentation.

SpreadsheetView class should be also usable in GWT without Vaadin Framework, but then you must implement SpreadsheetModel by yourself.


### Dependencies

- Apache POI 3.8 - http://poi.apache.org/
- Apache Commons Codec 1.5 - Required by POI - http://commons.apache.org/codec/


### Release notes

This is a "pre-alpha" quality release with severe limitations:

- All columns and rows have fixed sizes
- No cell styling is supported
- No merged cells are supported
- Performance for larger spreadsheets is really bad
- Only one spreadsheet widget is supported on screen at once


### License & Author

Apache License 2.0

Joonas Lehtinen - https://vaadin.com/joonas