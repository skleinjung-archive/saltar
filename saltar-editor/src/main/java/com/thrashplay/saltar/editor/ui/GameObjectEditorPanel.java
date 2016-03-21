package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.engine.GameObject;

import javax.swing.*;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GameObjectEditorPanel extends JPanel {
    private GameObject gameObject;

    public GameObjectEditorPanel() {
        this(null);
    }

    public GameObjectEditorPanel(GameObject gameObject) {
        add(new JLabel("GameObject Properties"));

        this.gameObject = gameObject;
        if (gameObject != null) {
            add(new GameObjectPropertiesPanel(gameObject));
            for (Object component : gameObject.getComponents()) {
                add(new ComponentPropertiesPanel(component.getClass().getSimpleName(), component));
            }
        }
    }
}
