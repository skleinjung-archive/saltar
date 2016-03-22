package com.thrashplay.saltar.editor.tool;

import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.editor.model.Project;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class EraseToolComponent extends AbstractTileSelectingTool {
    private Project project;
    private MouseTouchManager leftMouseButtonTouchManager;
    private GameObjectManager gameObjectManager;

    public EraseToolComponent(Project project, MouseTouchManager leftMouseButtonTouchManager, GameObjectManager gameObjectManager) {
        super(project, leftMouseButtonTouchManager, gameObjectManager);
        this.project = project;
        this.leftMouseButtonTouchManager = leftMouseButtonTouchManager;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        super.update(gameObject, delta);

        if (leftMouseButtonTouchManager.isDown()) {
            GameObject objectToRemove = project.getGameObject(project.getSelectedTileX(), project.getSelectedTileY());
            gameObjectManager.unregister(objectToRemove);
            project.eraseGameObject(project.getSelectedTileX(), project.getSelectedTileY());
        }
    }
}
