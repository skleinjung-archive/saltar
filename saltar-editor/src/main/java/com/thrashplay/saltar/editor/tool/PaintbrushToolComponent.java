package com.thrashplay.saltar.editor.tool;

import com.thrashplay.luna.api.component.RenderableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.luna.api.level.config.GameObjectConfig;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.editor.model.GameObjectFactory;
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
    private GameObjectFactory gameObjectFactory;

    public PaintbrushToolComponent(Project project, MouseTouchManager leftMouseButtonTouchManager, GameObjectManager gameObjectManager, GameObjectFactory gameObjectFactory) {
        super(project, leftMouseButtonTouchManager, gameObjectManager);
        this.project = project;
        this.leftMouseButtonTouchManager = leftMouseButtonTouchManager;
        this.gameObjectManager = gameObjectManager;
        this.gameObjectFactory = gameObjectFactory;
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        super.update(gameObject, delta);

        if (leftMouseButtonTouchManager.isDown() && project.getSelectedTemplate() != -1) {
            if (project.getGameObjectConfig(project.getSelectedTileX(), project.getSelectedTileY()) == null) {
                GameObjectConfig gameObjectConfig = project.createGameObjectConfig();
                if (gameObjectConfig != null) {
                    GameObject newGameObject = gameObjectFactory.createGameObject(project, gameObjectConfig);
                    gameObjectManager.register(newGameObject);
                }
            }
        }
    }

    @Override
    public void render(Graphics graphics, GameObject gameObject) {

    }
}
