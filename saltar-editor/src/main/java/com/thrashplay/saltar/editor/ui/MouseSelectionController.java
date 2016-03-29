package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.editor.model.Project;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MouseSelectionController implements UpdateableComponent {
    private Project project;
    private MouseTouchManager mouseTouchManager;
    private GameObjectManager gameObjectManager;

    public MouseSelectionController(Project project, MouseTouchManager mouseTouchManager, GameObjectManager gameObjectManager) {
        this.project = project;
        this.mouseTouchManager = mouseTouchManager;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if (mouseTouchManager.isDown()) {
            GameObject viewport = gameObjectManager.getGameObject(GameObjectIds.ID_VIEWPORT);
            if (viewport != null) {
                Position viewportPosition = viewport.getComponent(Position.class);

                // map screen coordinates
                int xCoordinate = mouseTouchManager.getX() + (int) viewportPosition.getX();
                int yCoordinate = mouseTouchManager.getY() + (int) viewportPosition.getY();

                int tileSize = project.getLevel().getTileSize();
                int tileX = xCoordinate / tileSize;
                int tileY = yCoordinate / tileSize;

                project.setSelectedTileX(tileX);
                project.setSelectedTileY(tileY);
            }
        }
    }
}
