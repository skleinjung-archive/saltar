package com.thrashplay.saltar.editor.ui;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PropertiesPanel extends JPanel {
    private String title;
    private Component component;

    public PropertiesPanel(String title) {
        this.title = title;

        add(new JLabel(title));
    }
}
