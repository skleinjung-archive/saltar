package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;

import java.util.List;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class GameObjectGridSelectionManager {
    private GameObjectManager gameObjectManager;
    private int tileWidth;
    private int tileHeight;

    public GameObjectGridSelectionManager(GameObjectManager gameObjectManager, int tileWidth, int tileHeight) {
        this.gameObjectManager = gameObjectManager;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public GameObject getGameObject(int tileX, int tileY) {
        int left = tileX * tileWidth;
        int top = tileY * tileHeight;
        int right = left + tileWidth - 1;
        int bottom = top + tileHeight - 1;

        List<GameObject> gameObjects = gameObjectManager.getGameObjects();
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);

            Position position = gameObject.getComponent(Position.class);
            if (position == null) {
                continue;
            }

            int x = (int) position.getX();
            int y = (int) position.getY();

            // our origin point is inside the tile
            if (x >= left && x <= right && y >= top && y <= bottom) {
                return gameObject;
            }
        }

        return null;
    }
}
