package com.thrashplay.saltar.editor.swing;

import javax.swing.*;
import java.net.URL;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SaltarToolbar extends JToolBar {
    public SaltarToolbar() {
        super(JToolBar.HORIZONTAL);
        setFloatable(false);

        ButtonGroup buttonGroup = new ButtonGroup();
        add(createButton(buttonGroup, "/icons/paint.png"));
        add(createButton(buttonGroup, "/icons/delete.png"));
    }

    private JToggleButton createButton(ButtonGroup buttonGroup, String iconPath) {
        URL imageUrl = SaltarToolbar.class.getResource(iconPath);

        JToggleButton button = new JToggleButton();
        button.setIcon(new ImageIcon(imageUrl));

        buttonGroup.add(button);

        return button;
    }
}
