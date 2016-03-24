package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.input.InputManager;
import com.thrashplay.luna.api.input.KeyCode;
import com.thrashplay.saltar.editor.model.Project;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class KeyboardGridNavigationController implements UpdateableComponent {
    private int KEY_REPEAT_DELAY = 66;

    private Project project;
    private InputManager inputManager;

    private Map<KeyCode, Long> keyDownTimes;

    public KeyboardGridNavigationController(InputManager inputManager, Project project) {
        this.project = project;
        this.inputManager = inputManager;

        keyDownTimes = new HashMap<>();
        keyDownTimes.put(KeyCode.KEY_LEFT_ARROW, 0L);
        keyDownTimes.put(KeyCode.KEY_RIGHT_ARROW, 0L);
        keyDownTimes.put(KeyCode.KEY_UP_ARROW, 0L);
        keyDownTimes.put(KeyCode.KEY_DOWN_ARROW, 0L);
        keyDownTimes.put(KeyCode.KEY_S, 0L);
        keyDownTimes.put(KeyCode.KEY_E, 0L);
        keyDownTimes.put(KeyCode.KEY_D, 0L);
        keyDownTimes.put(KeyCode.KEY_F, 0L);
    }

    @Override
    public void update(GameObject gameObject, long delta) {
        processInput(KeyCode.KEY_RIGHT_ARROW, RightMovementHandler);
        processInput(KeyCode.KEY_F, RightMovementHandler);

        processInput(KeyCode.KEY_LEFT_ARROW, LeftMovementHandler);
        processInput(KeyCode.KEY_S, LeftMovementHandler);

        processInput(KeyCode.KEY_UP_ARROW, UpMovementHandler);
        processInput(KeyCode.KEY_E, UpMovementHandler);

        processInput(KeyCode.KEY_DOWN_ARROW, DownMovementHandler);
        processInput(KeyCode.KEY_D, DownMovementHandler);

        if (project.getSelectedTileX() < 0) {
            project.setSelectedTileX(0);
        }
        if (project.getSelectedTileY() < 0) {
            project.setSelectedTileY(0);
        }
        if (project.getSelectedTileX() > project.getLevel().getGridSizeX()) {
            project.setSelectedTileX(project.getLevel().getGridSizeX());
        }
        if (project.getSelectedTileY() > project.getLevel().getGridSizeY()) {
            project.setSelectedTileY(project.getLevel().getGridSizeY());
        }
    }

    private void processInput(KeyCode keyCode, MovementHandler movementHandler) {
        if (inputManager.isKeyDown(keyCode)) {
            if (keyDelayMet(keyCode)) {
                movementHandler.moveSelection(project);
                keyDownTimes.put(keyCode, System.currentTimeMillis());
            }
        } else {
            keyDownTimes.put(keyCode, System.currentTimeMillis() - KEY_REPEAT_DELAY - 1);
        }
    }

    private boolean keyDelayMet(KeyCode keyCode) {
        return System.currentTimeMillis() - keyDownTimes.get(keyCode) > KEY_REPEAT_DELAY;
    }

    private static abstract class MovementHandler {
        public abstract void moveSelection(Project project);
    }

    private static final MovementHandler LeftMovementHandler = new MovementHandler() {
        @Override
        public void moveSelection(Project project) {
            project.setSelectedTileX(project.getSelectedTileX() - 1);
        }
    };
    private static final MovementHandler RightMovementHandler = new MovementHandler() {
        @Override
        public void moveSelection(Project project) {
            project.setSelectedTileX(project.getSelectedTileX() + 1);
        }
    };
    private static final MovementHandler UpMovementHandler = new MovementHandler() {
        @Override
        public void moveSelection(Project project) {
            project.setSelectedTileY(project.getSelectedTileY() - 1);
        }
    };
    private static final MovementHandler DownMovementHandler = new MovementHandler() {
        @Override
        public void moveSelection(Project project) {
            project.setSelectedTileY(project.getSelectedTileY() + 1);
        }
    };
}

