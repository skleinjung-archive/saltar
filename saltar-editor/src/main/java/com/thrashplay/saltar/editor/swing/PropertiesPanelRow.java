package com.thrashplay.saltar.editor.swing;

import com.thrashplay.luna.api.LunaException;
import org.apache.commons.beanutils.PropertyUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PropertiesPanelRow extends JPanel {
    private Object object;
    private String property;
    private boolean editable;

    private JLabel label;
    private JTextField textField;

    public PropertiesPanelRow(Object object, String property) {
        this(object, property, true);
    }

    public PropertiesPanelRow(Object object, String property, boolean editable) {
        super();

        this.object = object;
        this.property = property;
        this.editable = editable;

        label = new JLabel(property);
        textField = new JTextField();
        try {
            textField.setText(PropertyUtils.getProperty(object, property).toString());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new LunaException("Failed to create property editor for property '" + property + "' on bean '" + object + "': " + e.toString(), e);
        }



        int maximumHeight = (int) label.getMaximumSize().getHeight();
        int maximumWidth = (int) (label.getMaximumSize().getWidth() + textField.getMaximumSize().getWidth());
        setMaximumSize(new Dimension(maximumWidth, maximumHeight));
    }
}
