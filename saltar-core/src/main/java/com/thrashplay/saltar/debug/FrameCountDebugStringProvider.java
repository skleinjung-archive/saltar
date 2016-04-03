package com.thrashplay.saltar.debug;

import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.saltar.component.DebugStringRenderer;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class FrameCountDebugStringProvider implements DebugStringRenderer.DebugStringProvider, UpdateableComponent {
    public static int currentFrame = 0;

    @Override
    public String[] getDebugStrings(GameObjectManager gameObjectManager) {
        return new String[] { "Current Frame: " + currentFrame };
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        currentFrame++;
    }
}
