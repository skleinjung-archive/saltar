package com.thrashplay.saltar.editor.tool;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.editor.model.Project;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class PaintbrushToolComponent extends AbstractTileSelectingTool implements RenderableComponent {
    private Project project;
    private MouseTouchManager leftMouseButtonTouchManager;
    private GameObjectManager gameObjectManager;

    public PaintbrushToolComponent(Project project, MouseTouchManager leftMouseButtonTouchManager, GameObjectManager gameObjectManager) {
        super(project, leftMouseButtonTouchManager, gameObjectManager);
        this.project = project;
        this.leftMouseButtonTouchManager = leftMouseButtonTouchManager;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        super.update(gameObject, delta);

        if (leftMouseButtonTouchManager.isDown() && project.getSelectedTemplate() != -1) {
            GameObject newGameObject = project.createGameObjectFromSelectedTemplate();
            Position position = newGameObject.getComponent(Position.class);
            int tileSize = project.getLevel().getTileSize();
            position.setX(project.getSelectedTileX() * tileSize);
            position.setY(project.getSelectedTileY() * tileSize);

            gameObjectManager.register(newGameObject);
        }
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {

    }
}
