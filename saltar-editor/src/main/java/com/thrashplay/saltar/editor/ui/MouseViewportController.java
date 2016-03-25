package com.thrashplay.saltar.editor.ui;

import com.thrashplay.luna.api.component.Position;
import com.thrashplay.luna.api.component.UpdateableComponent;
import com.thrashplay.luna.api.engine.GameObject;
import com.thrashplay.luna.api.engine.GameObjectIds;
import com.thrashplay.luna.api.engine.GameObjectManager;
import com.thrashplay.luna.desktop.input.MouseTouchManager;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MouseViewportController implements UpdateableComponent {
    private MouseTouchManager mouseTouchManager;
    private GameObjectManager gameObjectManager;
    private int oldX = Integer.MIN_VALUE;
    private int oldY = Integer.MIN_VALUE;

    public MouseViewportController(MouseTouchManager mouseTouchManager, GameObjectManager gameObjectManager) {
        this.mouseTouchManager = mouseTouchManager;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void update(GameObject gameObject, float delta) {
        if (mouseTouchManager.isDown()) {
            if (mouseTouchManager.isDragging() && oldX != Integer.MIN_VALUE && oldY != Integer.MIN_VALUE) {
                int deltaX = mouseTouchManager.getX() - oldX;
                int deltaY = mouseTouchManager.getY() - oldY;

                GameObject viewport = gameObjectManager.getGameObject(GameObjectIds.ID_VIEWPORT);
                if (viewport != null) {
                    Position viewportPosition = viewport.getComponent(Position.class);
                    viewportPosition.setX(viewportPosition.getX() - deltaX / 2);
                    viewportPosition.setY(viewportPosition.getY() - deltaY / 2);
                }
            }

            oldX = mouseTouchManager.getX();
            oldY = mouseTouchManager.getY();
        } else {
            oldX = Integer.MIN_VALUE;
            oldY = Integer.MIN_VALUE;
        }
    }
}
