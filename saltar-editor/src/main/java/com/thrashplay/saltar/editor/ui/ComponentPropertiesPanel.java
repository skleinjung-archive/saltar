package com.thrashplay.saltar.editor.ui;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class ComponentPropertiesPanel extends PropertiesPanel {
    private Object component;

    public ComponentPropertiesPanel(String title, Object component) {
        super(title);
        this.component = component;
    }
}
