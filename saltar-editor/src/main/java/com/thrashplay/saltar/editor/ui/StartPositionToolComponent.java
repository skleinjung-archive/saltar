package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.tool.AbstractTileSelectingTool;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class StartPositionToolComponent extends AbstractTileSelectingTool implements RenderableComponent {
    private Project project;
    private MouseTouchManager leftMouseTouchManager;
    private GameObjectManager gameObjectManager;

    public StartPositionToolComponent(Project project, MouseTouchManager leftMouseTouchManager, GameObjectManager gameObjectManager) {
        super(project, leftMouseTouchManager, gameObjectManager);
        this.project = project;
        this.leftMouseTouchManager = leftMouseTouchManager;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        super.update(gameObject, delta);

        if (leftMouseTouchManager.isDown()) {

            int tileSize = project.getLevel().getTileSize();
            int x = project.getSelectedTileX() * tileSize;
            int y = project.getSelectedTileY() * tileSize + 9; // 9 is how much shorter the sprite is than the whole two tiles
            project.setStartX(x);
            project.setStartY(y);

            GameObject playerStartMarker = gameObjectManager.getGameObject("saltar-editor-playerStartMarker");
            Position position = playerStartMarker.getComponent(Position.class);
            position.setX(x);
            position.setY(y);
        }
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {

    }
}
