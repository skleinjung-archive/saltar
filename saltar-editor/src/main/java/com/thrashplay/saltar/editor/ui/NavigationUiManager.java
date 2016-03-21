package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.desktop.input.MouseTouchManager;
import com.thrashplay.saltar.Saltar;
import com.thrashplay.saltar.editor.model.IdGenerator;
import com.thrashplay.saltar.editor.model.Project;
import com.thrashplay.saltar.editor.model.ProjectChangeListener;
import com.thrashplay.saltar.editor.screen.GridRenderer;

/**
 * Registers and unregisteres navigation components such as a grid and mouse viewport.
 *
 * @author Sean Kleinjung
 */
public class NavigationUiManager implements ProjectChangeListener {
    private MouseTouchManager mouseTouchManager;
    private GameObjectManager gameObjectManager;
    private IdGenerator idGenerator;

    private GameObject grid;
    private GameObject vewport;

    public NavigationUiManager(MouseTouchManager mouseTouchManager, GameObjectManager gameObjectManager, IdGenerator idGenerator) {
        this.mouseTouchManager = mouseTouchManager;
        this.gameObjectManager = gameObjectManager;
        this.idGenerator = idGenerator;
    }

    @Override
    public void onProjectChanged(Project oldProject, Project newProject) {
        unregister(grid);
        unregister(vewport);

        if (newProject != null) {
            grid = new GameObject(idGenerator.getId("saltar-level-editor-grid"));
            grid.addComponent(new GridRenderer(newProject.getLevel(), 0xff999999));
            gameObjectManager.register(grid);

            vewport = new GameObject(GameObjectIds.ID_VIEWPORT);
            vewport.addComponent(new Position(0, 0, Saltar.SCENE_WIDTH, Saltar.SCENE_HEIGHT));
            vewport.addComponent(new MouseViewportController(mouseTouchManager, gameObjectManager));
            gameObjectManager.register(vewport);
        } else {
            grid = null;
            vewport = null;
        }
    }

    private void unregister(GameObject gameObject) {
        if (gameObject != null) {
            gameObjectManager.unregister(gameObject);
        }
    }
}
