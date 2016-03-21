package com.thrashplay.saltar.editor.swing;

import com.thrashplay.luna.api.LunaException;
import org.apache.commons.beanutils.PropertyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PropertiesPanel extends JPanel {
    private int currentRow = 0;

    public PropertiesPanel() {
        setLayout(new GridBagLayout());
    }

    protected void addSection(final String name) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = currentRow++;
        c.gridwidth = 2;
        c.insets = new Insets(15, 10, 10, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0f;
        JLabel sectionLabel = new JLabel(name);
        sectionLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        add(sectionLabel, c);

        currentRow++;
    }

    protected void addComment(final String text) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = currentRow++;
        c.gridwidth = 2;
        c.insets = new Insets(0, 5, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0f;
        add(new JLabel(text), c);

        currentRow++;
    }

    protected void addProperty(final Object object, final String name) {
        JLabel label = new JLabel(name);
        final JTextField textField = new JTextField();
        try {
            textField.setText(PropertyUtils.getProperty(object, name).toString());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new LunaException("Failed to create property editor for property '" + name + "' on bean '" + object + "': " + e.toString(), e);
        }

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPropertyEdited(object, name, textField.getText());
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = currentRow;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0.1f;
        add(label, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = currentRow++;
        c.insets = new Insets(5, 5, 0, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.9f;
        add(textField, c);
    }

    protected void addBottomBuffer() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = currentRow++;
        c.gridwidth = 2;
        c.weighty = 1.0f;
        add(new JLabel(), c);
    }

    public void onPropertyEdited(Object object, String propertyName, String propertyValue) {
        try {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(object, propertyName);
            if (String.class.equals(propertyDescriptor.getPropertyType())) {
                PropertyUtils.setProperty(object, propertyName, propertyValue);
            } else if (Integer.class.equals(propertyDescriptor.getPropertyType()) || int.class.equals(propertyDescriptor.getPropertyType())) {
                PropertyUtils.setProperty(object, propertyName, Integer.valueOf(propertyValue));
            } else if (Long.class.equals(propertyDescriptor.getPropertyType()) || long.class.equals(propertyDescriptor.getPropertyType())) {
                PropertyUtils.setProperty(object, propertyName, Long.valueOf(propertyValue));
            } else if (Float.class.equals(propertyDescriptor.getPropertyType()) || float.class.equals(propertyDescriptor.getPropertyType())) {
                PropertyUtils.setProperty(object, propertyName, Float.valueOf(propertyValue));
            } else if (Double.class.equals(propertyDescriptor.getPropertyType()) || double.class.equals(propertyDescriptor.getPropertyType())) {
                PropertyUtils.setProperty(object, propertyName, Double.valueOf(propertyValue));
            } else {
                throw new LunaException("Cannot set property of type '" + propertyDescriptor.getPropertyType() + "'");
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new LunaException("Failed to set property '" + propertyName + "' on bean '" + object + "' to value '" + propertyValue + "': " + e.toString(), e);
        }
    }
}
