package com.thrashplay.saltar.editor.screen;

import com.thrashplay.luna.api.engine.DefaultScreen;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MutableScreen extends DefaultScreen {
    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

    public void register(GameObject gameObject) {
        gameObjectManager.register(gameObject);
    }

    public void unregister(GameObject gameObject) {
        gameObjectManager.unregister(gameObject);
    }
}
