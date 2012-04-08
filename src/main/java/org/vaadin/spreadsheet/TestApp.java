package org.vaadin.spreadsheet;

import com.vaadin.Application;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class TestApp extends Application
{
    private Window window;

    @Override
    public void init()
    {
        window = new Window("Widget Test");
        setMainWindow(window);
        window.addComponent(new Spreadsheet());
    }
    
}
