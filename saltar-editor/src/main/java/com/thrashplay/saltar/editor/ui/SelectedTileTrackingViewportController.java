package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.saltar.editor.model.Project;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class SelectedTileTrackingViewportController implements UpdateableComponent {
    private Project project;
    private GameObjectManager gameObjectManager;

    private int oldSelectedTileX;
    private int oldSelectedTileY;

    public SelectedTileTrackingViewportController(Project project, GameObjectManager gameObjectManager) {
        this.project = project;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        // only update if the selection has changed, which allows the mouse to override the keyboard navigation
        if (oldSelectedTileX != project.getSelectedTileX() || oldSelectedTileY != project.getSelectedTileY()) {
            GameObject viewport = gameObjectManager.getGameObject(GameObjectIds.ID_VIEWPORT);
            if (viewport != null) {
                Position viewportPosition = viewport.getComponent(Position.class);

                int tileSize = project.getLevel().getTileSize();
                int selectedTileLeft = project.getSelectedTileX() * tileSize;
                int selectedTileRight = project.getSelectedTileX() * tileSize + tileSize - 1;
                int selectedTileTop = project.getSelectedTileY() * tileSize;
                int selectedTileBottom = project.getSelectedTileY() * tileSize + tileSize - 1;

                if (selectedTileLeft < viewportPosition.getLeft()) {
                    viewportPosition.setX(selectedTileLeft - tileSize / 2);
                }
                if (selectedTileTop < viewportPosition.getTop()) {
                    viewportPosition.setY(selectedTileTop - tileSize / 2);
                }
                if (selectedTileRight > viewportPosition.getRight()) {
                    viewportPosition.setX(selectedTileRight + tileSize / 2 - viewportPosition.getWidth());
                }
                if (selectedTileBottom > viewportPosition.getBottom()) {
                    viewportPosition.setY(selectedTileBottom + tileSize / 2 - viewportPosition.getHeight());
                }
            }
        }

        oldSelectedTileX = project.getSelectedTileX();
        oldSelectedTileY = project.getSelectedTileY();
    }
}
