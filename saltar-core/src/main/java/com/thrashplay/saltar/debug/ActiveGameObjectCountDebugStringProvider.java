package com.thrashplay.saltar.debug;

import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.saltar.component.DebugStringRenderer;

import java.util.List;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class ActiveGameObjectCountDebugStringProvider implements DebugStringRenderer.DebugStringProvider {
    @Override
    public String[] getDebugStrings(GameObjectManager gameObjectManager) {
        int activeCount = 0;

        List<GameObject> gameObjects  = gameObjectManager.getGameObjects();
        int len = gameObjects.size();
        for (int i = 0; i < len; i++) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject.isActive()) {
                activeCount++;
            }
        }

        return new String[] { "Active game objects: " + activeCount };
    }
}
