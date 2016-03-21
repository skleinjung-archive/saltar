package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.engine.GameObject;

import java.awt.*;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GameObjectPropertiesPanel extends PropertiesPanel {
    private GameObject gameObject;

    public GameObjectPropertiesPanel(GameObject gameObject) {
        super("GameObject Properties");
        this.gameObject = gameObject;
    }
}
