package com.thrashplay.saltar.editor.swing;

import com.thrashplay.luna.api.engine.GameObject;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GameObjectPropertiesPanel extends PropertiesPanel {
    private GameObject gameObject;

    public GameObjectPropertiesPanel(GameObject gameObject) {
        this.gameObject = gameObject;
        updateProperties(gameObject);
    }

    private void updateProperties(GameObject gameObject) {
        removeAll();

        if (gameObject != null) {
            addSection("GameObject Properties");
            addProperty(gameObject, "id");
            addProperty(gameObject, "renderLayer");

            for (Object component : gameObject.getComponents()) {
                addSection(component.getClass().getSimpleName() + " Properties");
                int propertyCount = 0;
                for (PropertyDescriptor propertyDescriptor : PropertyUtils.getPropertyDescriptors(component)) {
                    if ("class".equals(propertyDescriptor.getName())) {
                        continue;
                    }

                    propertyCount++;
                    addProperty(component, propertyDescriptor.getName());
                }

                if (propertyCount < 1) {
                    addComment("No editable properties");
                }
            }
        }

        addBottomBuffer();
    }
}
